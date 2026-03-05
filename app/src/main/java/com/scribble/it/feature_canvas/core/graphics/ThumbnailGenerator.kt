package com.scribble.it.feature_canvas.core.graphics

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import com.scribble.it.feature_canvas.core.graphics.error.GenerateErrorType
import com.scribble.it.feature_canvas.core.graphics.error.SaveErrorType
import com.scribble.it.feature_canvas.core.graphics.error.ThumbnailError
import com.scribble.it.feature_canvas.domain.fileManager.FileManager
import com.scribble.it.feature_canvas.domain.model.canvas.CanvasDrawing
import com.scribble.it.feature_canvas.domain.model.stroke.CanvasStroke
import com.scribble.it.feature_canvas.domain.model.stroke.PEN
import com.scribble.it.ui.adaptive.scale.CanvasConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class ThumbnailGenerator @Inject constructor(
    @ApplicationContext val context: Context,
    private val fileManager: FileManager
) {
    private val thumbnailWidth: Int = 512

    suspend fun generateAndSave(
        canvasDrawing: CanvasDrawing,
    ): String = withContext(Dispatchers.Default) {
        val canvasStrokes = canvasDrawing.canvasStrokes
        val pageAspectRatio = canvasDrawing.pageFormat.aspectRatio
        val pageColor = canvasDrawing.pageColor
        val existingThumbnailPath = canvasDrawing.thumbnailPath

        if (canvasStrokes.isEmpty()) throw ThumbnailError.EmptyDataError

        try {
            val bitmap = generate(canvasStrokes, pageColor, pageAspectRatio)
            fileManager.saveFile(bitmap, existingThumbnailPath)
        } catch (e: ThumbnailError) {
            throw e
        } catch (e: OutOfMemoryError) {
            throw ThumbnailError.GenerateError(e, GenerateErrorType.BITMAP_CREATION_FAILED)
        } catch (e: IllegalArgumentException) {
            throw ThumbnailError.GenerateError(e, GenerateErrorType.CALCULATION_ERROR)
        } catch (e: Exception) {
            Log.e("ThumbnailGenerator", "Error generating thumbnail", e)
            throw ThumbnailError.GenerateError(e, GenerateErrorType.UNKNOWN)
        }
    }

    private fun generate(
        canvasStrokes: List<CanvasStroke>,
        canvasPageColor: Int,
        aspectRatio: Float
    ): Bitmap {

        val width = thumbnailWidth
        val height = (width / aspectRatio).toInt()

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(canvasPageColor)

        if (canvasStrokes.isEmpty()) return bitmap

        drawPaths(canvas, canvasStrokes, width, height)
        return bitmap
    }

    private fun drawPaths(
        canvas: Canvas,
        strokes: List<CanvasStroke>,
        width: Int,
        height: Int
    ) {
        val scaleX = width / CanvasConstants.PAGE_WIDTH
        val scaleY = height / CanvasConstants.PAGE_HEIGHT

        val path = Path()

        var currentColor = strokes.first().colorArgb
        var currentBrush = strokes.first().brushSizeNormalized

        val linePaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
            color = currentColor
            strokeWidth = currentBrush
        }

        val dotPaint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = currentColor
        }

        for (stroke in strokes) {

            val x = stroke.x * scaleX
            val y = stroke.y * scaleY

            if (stroke.colorArgb != currentColor || stroke.brushSizeNormalized != currentBrush) {
                linePaint.strokeWidth = currentBrush * width
                canvas.drawPath(path, linePaint)
                path.reset()

                currentColor = stroke.colorArgb
                currentBrush = stroke.brushSizeNormalized

                linePaint.color = currentColor
                linePaint.strokeWidth = currentBrush
                dotPaint.color = currentColor
            }

            when (stroke.penType) {
                PEN.MOVE -> path.moveTo(x, y)
                PEN.DRAW -> path.lineTo(x, y)
                PEN.DOT -> canvas.drawCircle(x, y, currentBrush * width, dotPaint)
            }
        }

        linePaint.strokeWidth = currentBrush * width
        canvas.drawPath(path, linePaint)
    }

}
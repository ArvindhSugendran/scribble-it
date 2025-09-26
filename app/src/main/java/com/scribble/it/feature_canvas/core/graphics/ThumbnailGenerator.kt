package com.scribble.it.feature_canvas.core.graphics

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.Log
import androidx.compose.ui.geometry.Offset
import com.scribble.it.feature_canvas.core.graphics.error.GenerateErrorType
import com.scribble.it.feature_canvas.core.graphics.error.SaveErrorType
import com.scribble.it.feature_canvas.core.graphics.error.ThumbnailError
import com.scribble.it.feature_canvas.domain.model.stroke.CanvasStroke
import com.scribble.it.feature_canvas.domain.model.stroke.PEN
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class ThumbnailGenerator @Inject constructor(
    @ApplicationContext val context: Context
) {
    private val thumbnailWidth: Int = 400
    private val thumbnailHeight: Int = 400

    suspend fun generateAndSave(
        canvasStrokes: List<CanvasStroke>
    ): String = withContext(Dispatchers.Default) {
        if(canvasStrokes.isEmpty()) throw ThumbnailError.EmptyDataError

        try {
            val bitmap = generate(canvasStrokes)

            withContext(Dispatchers.IO) {
                saveBitmapToFile(bitmap)
            }
        }
        catch (e: ThumbnailError) {
            throw e
        }
        catch (e: OutOfMemoryError) {
            throw ThumbnailError.GenerateError(e, GenerateErrorType.BITMAP_CREATION_FAILED)
        }
        catch (e: IllegalArgumentException) {
            throw ThumbnailError.GenerateError(e, GenerateErrorType.CALCULATION_ERROR)
        }
        catch (e: Exception) {
            Log.e("ThumbnailGenerator", "Error generating thumbnail", e)
            throw ThumbnailError.GenerateError(e, GenerateErrorType.UNKNOWN)
        }
    }

    private fun generate(
        canvasStrokes: List<CanvasStroke>
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(thumbnailWidth, thumbnailHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        if(canvasStrokes.isEmpty()) return bitmap

        val bounds = calculateDrawingBounds(canvasStrokes)
        val scale = calculateScaleFactor(bounds)
        val offset = calculateCenteringOffset(bounds, scale)

        val defaultLinePaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
        }

        val defaultDotPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        // Apply transformations
        canvas.save()
        canvas.translate(offset.x, offset.y)
        canvas.scale(scale, scale)

        // Draw all elements
        drawPaths(canvas, canvasStrokes, defaultLinePaint, defaultDotPaint)

        canvas.restore()
        return bitmap
    }

    private fun drawPaths(
        canvas: Canvas,
        canvasStrokes: List<CanvasStroke>,
        defaultLinePaint: Paint,
        defaultDotPaint: Paint
    ) {
        val path = Path()
        var currentColor = canvasStrokes[0].color
        var currentStrokePx = canvasStrokes[0].strokePx

        val linePaint = Paint(defaultLinePaint).apply {
            color = currentColor
            strokeWidth = currentStrokePx
        }

        val dotPaint = Paint(defaultDotPaint).apply {
            color = currentColor
        }

        for(stroke in canvasStrokes) {
            if(stroke.color != currentColor || stroke.strokePx != currentStrokePx) {
                canvas.drawPath(path, linePaint)
                path.reset()

                currentColor = stroke.color
                currentStrokePx = stroke.strokePx

                linePaint.color = currentColor
                linePaint.strokeWidth = currentStrokePx
                dotPaint.color = currentColor
            }

            when(stroke.pen) {
                PEN.MOVE -> {
                    path.moveTo(stroke.xCoordinate, stroke.yCoordinate)
                }
                PEN.DRAW -> {
                    path.lineTo(stroke.xCoordinate, stroke.yCoordinate)
                }
                PEN.DOT -> {
                    canvas.drawCircle(stroke.xCoordinate, stroke.yCoordinate, currentStrokePx, dotPaint)
                }
            }
        }

        canvas.drawPath(path,linePaint)
    }

    private fun calculateDrawingBounds(canvasStrokes: List<CanvasStroke>): RectF {
        val minX = canvasStrokes.minOf { it.xCoordinate }
        val maxX = canvasStrokes.maxOf { it.xCoordinate }
        val minY = canvasStrokes.minOf { it.yCoordinate }
        val maxY = canvasStrokes.maxOf { it.yCoordinate }

        return RectF(minX, minY, maxX, maxY).apply {
            inset(-width() * 0.1f, -height() * 0.1f)
        }
    }

    private fun calculateScaleFactor(bounds: RectF): Float {
        val scaleX = thumbnailWidth / bounds.width()
        val scaleY = thumbnailHeight / bounds.height()
        return minOf(scaleX, scaleY, 1f)
    }

    private fun calculateCenteringOffset(bounds: RectF, scale: Float): Offset {
        val scaledWidth = bounds.width() * scale
        val scaledHeight = bounds.height() * scale
        val offsetX = (thumbnailWidth - scaledWidth) / 2 - bounds.left * scale
        val offsetY = (thumbnailHeight - scaledHeight) / 2 - bounds.right * scale
        return Offset(offsetX, offsetY)
    }

    private fun saveBitmapToFile(
        bitmap: Bitmap
    ): String {
       try {
           val file = File(context.filesDir, "scribble/thumbnails").apply { mkdirs() }
           val outputFile = File(file, "thumbnail_${System.currentTimeMillis()}.png")

           FileOutputStream(outputFile).use { out->
              val isCompressed = bitmap.compress(Bitmap.CompressFormat.PNG, 85, out)
               if(!isCompressed) {
                   throw ThumbnailError.SaveError(errorType = SaveErrorType.IO_ERROR)
               }
           }

           Log.d("THUMBNAIL_PATH", "Saved thumbnail: ${outputFile.absolutePath}")
           bitmap.recycle()

           return outputFile.absolutePath
       }
       catch (e: ThumbnailError) {
           throw e
       }
       catch (e: IOException) {
           throw ThumbnailError.SaveError(e, SaveErrorType.IO_ERROR)
       }
       catch (e: SecurityException) {
           throw ThumbnailError.SaveError(e, SaveErrorType.SECURITY_ERROR)
       }
       catch (e: Exception) {
           Log.e("ThumbnailGenerator", "Unknown error saving thumbnail", e)
           throw ThumbnailError.SaveError(e, SaveErrorType.UNKNOWN)
       }

    }

}
package com.scribble.it.feature_canvas.data.fileManager

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.scribble.it.feature_canvas.core.graphics.error.SaveErrorType
import com.scribble.it.feature_canvas.core.graphics.error.ThumbnailError
import com.scribble.it.feature_canvas.domain.fileManager.FileManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class FileManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FileManager {
    override suspend fun saveFile(bitmap: Bitmap, existingThumbnailPath: String?): String = withContext(Dispatchers.IO) {
        try {
            val fileDir = File(context.filesDir, "scribble/thumbnails").apply { mkdirs() }

            val outputFile = if (!existingThumbnailPath.isNullOrBlank()) {
                File(existingThumbnailPath)
            } else {
                File(fileDir, "thumbnail_${System.currentTimeMillis()}.png")
            }

            FileOutputStream(outputFile).use { out ->
                val isCompressed = bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                if (!isCompressed) {
                    throw ThumbnailError.SaveError(errorType = SaveErrorType.IO_ERROR)
                }
            }

            Log.d("ThumbnailGenerator", "Saved thumbnail: ${outputFile.absolutePath}")
            outputFile.absolutePath
        } catch (e: ThumbnailError) {
            throw e
        } catch (e: IOException) {
            throw ThumbnailError.SaveError(e, SaveErrorType.IO_ERROR)
        } catch (e: SecurityException) {
            throw ThumbnailError.SaveError(e, SaveErrorType.SECURITY_ERROR)
        } catch (e: Exception) {
            Log.e("ThumbnailGenerator", "Unknown error saving thumbnail", e)
            throw ThumbnailError.SaveError(e, SaveErrorType.UNKNOWN)
        }
        finally {
            bitmap.recycle()
        }
    }

    override suspend fun deleteFile(path: String) = withContext(Dispatchers.IO) {
        try {
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: IOException) {
            throw e
        }catch (e: Exception) {
            throw e
        }
    }
}
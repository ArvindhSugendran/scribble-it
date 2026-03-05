package com.scribble.it.feature_canvas.domain.fileManager

import android.graphics.Bitmap

interface FileManager {
    suspend fun saveFile(bitmap: Bitmap, existingThumbnailPath: String?): String

    suspend fun deleteFile(path: String)
}
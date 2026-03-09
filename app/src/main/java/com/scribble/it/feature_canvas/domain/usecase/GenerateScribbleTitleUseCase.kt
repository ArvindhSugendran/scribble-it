package com.scribble.it.feature_canvas.domain.usecase

import com.scribble.it.feature_canvas.domain.repository.CanvasRepository
import javax.inject.Inject

class GenerateScribbleTitleUseCase @Inject constructor(
    private val canvasRepository: CanvasRepository
) {
    suspend operator fun invoke(): Pair<String, Int> {
        return try {
            val maxIndex = canvasRepository.getMaxAutoTitleIndex() ?: 0
            val nextIndex = maxIndex + 1
            return "Scribble %02d".format(nextIndex) to nextIndex
        } catch (e: Exception) {
            "Scribble" to 0
        }
    }
}
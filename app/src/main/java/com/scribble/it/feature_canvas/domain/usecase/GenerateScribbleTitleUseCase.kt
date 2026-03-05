package com.scribble.it.feature_canvas.domain.usecase

import com.scribble.it.feature_canvas.domain.repository.CanvasRepository
import javax.inject.Inject

class GenerateScribbleTitleUseCase @Inject constructor(
    private val canvasRepository: CanvasRepository
) {
    suspend operator fun invoke(): String {
        return try {
            val max = canvasRepository.generateScribbleTitle() ?: 0
            val next = max + 1
            "Scribble %02d".format(next)
        } catch (e: Exception) {
            "Scribble"
        }
    }
}
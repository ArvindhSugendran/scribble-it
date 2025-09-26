package com.scribble.it.feature_canvas.data.mappers

import com.scribble.it.feature_canvas.data.local.db.entities.CanvasEntity
import com.scribble.it.feature_canvas.data.local.db.model.CanvasStrokeDto
import com.scribble.it.feature_canvas.domain.model.canvas.CanvasDrawing
import com.scribble.it.feature_canvas.domain.model.stroke.CanvasStroke
import com.scribble.it.feature_canvas.domain.model.stroke.PEN

fun CanvasDrawing.toCanvasEntity(): CanvasEntity {
    return CanvasEntity(
        id = this.id,
        title = this.title,
        canvasStrokesDto = this.canvasStrokes.map { it.toCanvasStrokeDto() },
        thumbnailPath = this.thumbnailPath,
        createdDate = this.createdDate,
        modifiedDate = this.modifiedDate,
        deletedAt = this.deletedAt
    )
}

fun CanvasEntity.toCanvasDrawing(): CanvasDrawing {
    return CanvasDrawing(
        id = this.id,
        title = this.title,
        canvasStrokes = this.canvasStrokesDto.map { it.toCanvasStroke() },
        thumbnailPath = this.thumbnailPath,
        createdDate = this.createdDate,
        modifiedDate = this.modifiedDate,
        deletedAt = this.deletedAt
    )
}

fun CanvasStroke.toCanvasStrokeDto(): CanvasStrokeDto {
    return CanvasStrokeDto(
        xCoordinate = this.xCoordinate,
        yCoordinate = this.yCoordinate,
        pen = this.pen.name,
        strokePx = this.strokePx,
        color = this.color
    )
}

fun CanvasStrokeDto.toCanvasStroke(): CanvasStroke {
    return CanvasStroke(
        xCoordinate = this.xCoordinate,
        yCoordinate = this.yCoordinate,
        pen = PEN.valueOf(this.pen),
        strokePx = this.strokePx,
        color = this.color
    )
}


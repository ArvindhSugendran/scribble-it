package com.scribble.it.feature_canvas.data.mappers

import com.scribble.it.feature_canvas.data.local.db.entities.CanvasEntity
import com.scribble.it.feature_canvas.data.local.db.model.CanvasStrokeEntity
import com.scribble.it.feature_canvas.data.local.db.model.CanvasSummaryEntity
import com.scribble.it.feature_canvas.domain.model.canvas.CanvasDrawing
import com.scribble.it.feature_canvas.domain.model.canvasSummary.CanvasSummary
import com.scribble.it.feature_canvas.domain.model.stroke.CanvasStroke
import com.scribble.it.feature_canvas.domain.model.stroke.PEN

fun CanvasDrawing.toCanvasEntity(): CanvasEntity {
    return CanvasEntity(
        id = this.id,
        autoTitleIndex = this.autoTitleIndex,
        title = this.title,
        canvasStrokesDto = this.canvasStrokes.map { it.toCanvasStrokeDto() },
        pageFormat = this.pageFormat,
        pageColor = this.pageColor,
        thumbnailPath = this.thumbnailPath,
        createdDate = this.createdDate,
        modifiedDate = this.modifiedDate,
        deletedDate = this.deletedDate
    )
}

fun CanvasSummaryEntity.toCanvasSummary(): CanvasSummary {
    return CanvasSummary(
        id = this.id,
        title = this.title,
        thumbnailPath = this.thumbnailPath,
        createdDate = this.createdDate,
        modifiedDate = this.modifiedDate,
        deletedDate = this.deletedDate
    )
}

fun CanvasEntity.toCanvasDrawing(): CanvasDrawing {
    return CanvasDrawing(
        id = this.id,
        autoTitleIndex = this.autoTitleIndex,
        title = this.title,
        canvasStrokes = this.canvasStrokesDto.map { it.toCanvasStroke() },
        pageFormat = this.pageFormat,
        pageColor = this.pageColor,
        thumbnailPath = this.thumbnailPath,
        createdDate = this.createdDate,
        modifiedDate = this.modifiedDate,
        deletedDate = this.deletedDate
    )
}

fun CanvasStroke.toCanvasStrokeDto(): CanvasStrokeEntity {
    return CanvasStrokeEntity(
        x = this.x,
        y = this.y,
        penType = this.penType.name,
        brushSizePx = this.brushSizePx,
        brushSizeNormalized = this.brushSizeNormalized,
        colorArgb = this.colorArgb
    )
}

fun CanvasStrokeEntity.toCanvasStroke(): CanvasStroke {
    return CanvasStroke(
        x = this.x,
        y = this.y,
        penType = PEN.valueOf(this.penType),
        brushSizePx = this.brushSizePx,
        brushSizeNormalized = this.brushSizeNormalized,
        colorArgb = this.colorArgb
    )
}


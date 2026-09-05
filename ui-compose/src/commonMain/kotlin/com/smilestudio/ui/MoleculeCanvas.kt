package com.smilestudio.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import com.smilestudio.core.BondType
import com.smilestudio.core.Molecule
import com.smilestudio.core.Point2D

private const val DEFAULT_SCALE = 60f
private const val MAX_SCALE = 120f
private const val FIT_MARGIN_FRACTION = 0.85f
private const val MIN_MOLECULE_EXTENT = 1e-3f
private const val STROKE_WIDTH_PX = 3f
private const val PARALLEL_LINE_SPACING_PX = STROKE_WIDTH_PX * 2.5f

/**
 * Scale (pixels per layout unit) that fits a molecule of the given size within [FIT_MARGIN_FRACTION]
 * of the available canvas, preserving aspect ratio. Falls back to [DEFAULT_SCALE] for a
 * zero-extent molecule (e.g. a single isolated atom), and never exceeds [MAX_SCALE].
 */
fun computeFitScale(moleculeWidth: Float, moleculeHeight: Float, canvasWidth: Float, canvasHeight: Float): Float {
    if (moleculeWidth < MIN_MOLECULE_EXTENT && moleculeHeight < MIN_MOLECULE_EXTENT) {
        return DEFAULT_SCALE
    }
    val scaleX = if (moleculeWidth < MIN_MOLECULE_EXTENT) Float.MAX_VALUE else (canvasWidth * FIT_MARGIN_FRACTION) / moleculeWidth
    val scaleY = if (moleculeHeight < MIN_MOLECULE_EXTENT) Float.MAX_VALUE else (canvasHeight * FIT_MARGIN_FRACTION) / moleculeHeight
    return minOf(scaleX, scaleY, MAX_SCALE)
}

@Composable
fun MoleculeCanvas(molecule: Molecule?, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier) {
        if (molecule == null) return@Canvas
        val commands = planMoleculeDrawing(molecule)
        if (commands.isEmpty()) return@Canvas

        val toCanvas = canvasMapper(commands, size.width, size.height)

        for (command in commands) {
            when (command) {
                is DrawCommand.BondLine -> drawBondLine(command, toCanvas)
                is DrawCommand.AtomLabel -> {
                    val textLayout = textMeasurer.measure(command.text)
                    val center = toCanvas(command.position)
                    val topLeft = center - Offset(textLayout.size.width / 2f, textLayout.size.height / 2f)
                    // Clear the bond line(s) passing under the label so the text stays legible.
                    drawRect(
                        color = Color.White,
                        topLeft = topLeft,
                        size = Size(textLayout.size.width.toFloat(), textLayout.size.height.toFloat()),
                    )
                    drawText(textLayout, topLeft = topLeft)
                }
            }
        }
    }
}

private fun canvasMapper(commands: List<DrawCommand>, canvasWidth: Float, canvasHeight: Float): (Point2D) -> Offset {
    val points = commands.flatMap { command ->
        when (command) {
            is DrawCommand.BondLine -> listOf(command.from, command.to)
            is DrawCommand.AtomLabel -> listOf(command.position)
        }
    }
    val minX = points.minOf { it.x }
    val maxX = points.maxOf { it.x }
    val minY = points.minOf { it.y }
    val maxY = points.maxOf { it.y }
    val moleculeCenterX = (minX + maxX) / 2
    val moleculeCenterY = (minY + maxY) / 2

    val scale = computeFitScale(
        moleculeWidth = (maxX - minX).toFloat(),
        moleculeHeight = (maxY - minY).toFloat(),
        canvasWidth = canvasWidth,
        canvasHeight = canvasHeight,
    )
    val canvasCenter = Offset(canvasWidth / 2f, canvasHeight / 2f)

    return { point ->
        canvasCenter + Offset(
            x = (point.x - moleculeCenterX).toFloat() * scale,
            y = -(point.y - moleculeCenterY).toFloat() * scale,
        )
    }
}

private fun DrawScope.drawBondLine(command: DrawCommand.BondLine, toCanvas: (Point2D) -> Offset) {
    val from = toCanvas(command.from)
    val to = toCanvas(command.to)
    val lineCount = when (command.bondType) {
        BondType.SINGLE, BondType.AROMATIC -> 1
        BondType.DOUBLE -> 2
        BondType.TRIPLE -> 3
    }

    if (lineCount == 1) {
        drawLine(Color.Black, from, to, strokeWidth = STROKE_WIDTH_PX)
        return
    }

    val direction = to - from
    val length = direction.getDistance()
    if (length == 0f) {
        drawLine(Color.Black, from, to, strokeWidth = STROKE_WIDTH_PX)
        return
    }
    val perpendicular = Offset(-direction.y, direction.x) / length
    val totalWidth = PARALLEL_LINE_SPACING_PX * (lineCount - 1)
    for (i in 0 until lineCount) {
        val offset = perpendicular * (PARALLEL_LINE_SPACING_PX * i - totalWidth / 2f)
        drawLine(Color.Black, from + offset, to + offset, strokeWidth = STROKE_WIDTH_PX)
    }
}

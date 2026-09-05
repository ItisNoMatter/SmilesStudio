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

private const val PIXELS_PER_UNIT = 60f
private const val STROKE_WIDTH_PX = 3f
private const val PARALLEL_LINE_SPACING_PX = STROKE_WIDTH_PX * 2.5f

@Composable
fun MoleculeCanvas(molecule: Molecule?, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier) {
        if (molecule == null) return@Canvas
        val commands = planMoleculeDrawing(molecule)
        if (commands.isEmpty()) return@Canvas

        val toCanvas = canvasMapper(commands, Offset(size.width / 2f, size.height / 2f))

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

private fun canvasMapper(commands: List<DrawCommand>, canvasCenter: Offset): (Point2D) -> Offset {
    val points = commands.flatMap { command ->
        when (command) {
            is DrawCommand.BondLine -> listOf(command.from, command.to)
            is DrawCommand.AtomLabel -> listOf(command.position)
        }
    }
    val moleculeCenterX = (points.minOf { it.x } + points.maxOf { it.x }) / 2
    val moleculeCenterY = (points.minOf { it.y } + points.maxOf { it.y }) / 2

    return { point ->
        canvasCenter + Offset(
            x = (point.x - moleculeCenterX).toFloat() * PIXELS_PER_UNIT,
            y = -(point.y - moleculeCenterY).toFloat() * PIXELS_PER_UNIT,
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

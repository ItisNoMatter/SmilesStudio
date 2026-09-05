package com.smilestudio.ui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MoleculeCanvasScalingTest {

    @Test
    fun `小さい分子はCanvasいっぱいまで拡大される`() {
        val scale = computeFitScale(
            moleculeWidth = 2f,
            moleculeHeight = 1f,
            canvasWidth = 800f,
            canvasHeight = 600f,
        )

        assertTrue(scale > 60f, "expected the molecule to be scaled up well beyond a fixed 60px/unit baseline")
    }

    @Test
    fun `大きい分子はCanvasに収まるよう縮小される`() {
        val scale = computeFitScale(
            moleculeWidth = 40f,
            moleculeHeight = 30f,
            canvasWidth = 400f,
            canvasHeight = 300f,
        )

        val scaledWidth = 40f * scale
        val scaledHeight = 30f * scale
        assertTrue(scaledWidth <= 400f, "scaled width ($scaledWidth) must fit within the canvas width")
        assertTrue(scaledHeight <= 300f, "scaled height ($scaledHeight) must fit within the canvas height")
    }

    @Test
    fun `縦横比の異なる分子は小さい方のスケールに合わせられアスペクト比が保たれる`() {
        // A molecule much wider than the canvas is wide, but well within the canvas height.
        val scale = computeFitScale(
            moleculeWidth = 100f,
            moleculeHeight = 1f,
            canvasWidth = 400f,
            canvasHeight = 400f,
        )

        val scaledWidth = 100f * scale
        assertTrue(scaledWidth <= 400f, "the width-constrained dimension must still fit")
    }

    @Test
    fun `孤立した原子のようにサイズがゼロの場合デフォルトスケールが使われる`() {
        val scale = computeFitScale(
            moleculeWidth = 0f,
            moleculeHeight = 0f,
            canvasWidth = 800f,
            canvasHeight = 600f,
        )

        assertEquals(60f, scale)
    }

    @Test
    fun `極端に小さい分子でもスケールは上限でクランプされる`() {
        val scale = computeFitScale(
            moleculeWidth = 0.001f,
            moleculeHeight = 0.001f,
            canvasWidth = 2000f,
            canvasHeight = 2000f,
        )

        assertTrue(scale <= 120f, "scale must not exceed the cap even for a tiny molecule")
    }
}

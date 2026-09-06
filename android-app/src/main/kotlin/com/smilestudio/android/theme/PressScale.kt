package com.smilestudio.android.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

private const val PRESSED_SCALE = 0.92f

/**
 * Slight press-down scale feedback (on top of a component's own ripple), animated with the
 * current MotionScheme so it inherits the expressive spring feel.
 *
 * [interactionSource] must be the *same* instance passed to the tappable component's own
 * `interactionSource` parameter -- otherwise this never observes a press and the scale never
 * animates.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Modifier.expressivePressScale(interactionSource: MutableInteractionSource): Modifier {
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) PRESSED_SCALE else 1f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "expressivePressScale",
    )
    return this.scale(scale)
}

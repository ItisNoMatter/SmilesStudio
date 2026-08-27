package com.smilestudio.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.smilestudio.core.Molecule

@Composable
fun MoleculeCanvas(molecule: Molecule?, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        // TODO: draw atoms and bonds from `molecule`
    }
}

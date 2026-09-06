package com.smilestudio.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.smilestudio.core.Molecule
import com.smilestudio.core.ParseResult
import com.smilestudio.core.SmilesParser

data class MoleculeEditorState(val molecule: Molecule?, val errorMessage: String?)

fun resolveMoleculeEditorState(smilesText: String, previousMolecule: Molecule?): MoleculeEditorState {
    if (smilesText.isBlank()) {
        return MoleculeEditorState(molecule = previousMolecule, errorMessage = null)
    }
    return when (val result = SmilesParser.parse(smilesText)) {
        is ParseResult.Success -> MoleculeEditorState(molecule = result.molecule, errorMessage = null)
        is ParseResult.Failure -> MoleculeEditorState(molecule = previousMolecule, errorMessage = result.reason)
    }
}

@Composable
fun MoleculeEditor(smilesText: String, onSmilesTextChange: (String) -> Unit, modifier: Modifier = Modifier) {
    var previousMolecule by remember { mutableStateOf<Molecule?>(null) }
    val state = resolveMoleculeEditorState(smilesText, previousMolecule)
    previousMolecule = state.molecule

    // WindowInsets.safeDrawing already includes ime, so this alone covers both the
    // keyboard-closed (nav bar/gesture inset) and keyboard-open cases -- adding a
    // separate imePadding() on top double-counts the keyboard height.
    Column(
        modifier = modifier.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)),
    ) {
        MoleculeCanvas(molecule = state.molecule, modifier = Modifier.weight(1f).fillMaxWidth())
        state.errorMessage?.let { Text(it) }
        TextField(value = smilesText, onValueChange = onSmilesTextChange, modifier = Modifier.fillMaxWidth())
    }
}

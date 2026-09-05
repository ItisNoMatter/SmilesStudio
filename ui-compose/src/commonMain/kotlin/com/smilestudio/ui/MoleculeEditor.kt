package com.smilestudio.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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

    Column(modifier = modifier) {
        TextField(value = smilesText, onValueChange = onSmilesTextChange, modifier = Modifier.fillMaxWidth())
        state.errorMessage?.let { Text(it) }
        MoleculeCanvas(molecule = state.molecule, modifier = Modifier.weight(1f).fillMaxWidth())
    }
}

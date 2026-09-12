package com.smilestudio.android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smilestudio.core.Molecule
import com.smilestudio.ui.MoleculeCanvas
import com.smilestudio.ui.resolveMoleculeEditorState

private val TextFieldShape = RoundedCornerShape(16.dp)

@Composable
fun HomeContent(
    smilesText: String,
    onSmilesTextChange: (String) -> Unit,
    onImageRecognitionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var previousMolecule by remember { mutableStateOf<Molecule?>(null) }
    val state = resolveMoleculeEditorState(smilesText, previousMolecule)
    previousMolecule = state.molecule

    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            MoleculeCanvas(molecule = state.molecule, modifier = Modifier.fillMaxSize())
            FloatingActionButton(
                onClick = onImageRecognitionClick,
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            ) {
                Icon(Icons.Rounded.AddAPhoto, contentDescription = "画像から認識")
            }
        }
        OutlinedTextField(
            value = smilesText,
            onValueChange = onSmilesTextChange,
            label = { Text("Smilesを入力") },
            isError = state.errorMessage != null,
            supportingText = state.errorMessage?.let { message ->
                { Text(message, style = MaterialTheme.typography.bodySmall) }
            },
            shape = TextFieldShape,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            ),
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        )
    }
}

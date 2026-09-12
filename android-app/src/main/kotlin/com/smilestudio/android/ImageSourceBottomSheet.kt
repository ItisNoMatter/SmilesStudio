package com.smilestudio.android

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSourceBottomSheet(onTakePhoto: () -> Unit, onPickFromGallery: () -> Unit, onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        ListItem(
            headlineContent = { Text("カメラで撮影") },
            leadingContent = { Icon(Icons.Rounded.CameraAlt, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().clickable(onClick = onTakePhoto),
        )
        ListItem(
            headlineContent = { Text("ギャラリーから選択") },
            leadingContent = { Icon(Icons.Rounded.PhotoLibrary, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().clickable(onClick = onPickFromGallery),
        )
    }
}

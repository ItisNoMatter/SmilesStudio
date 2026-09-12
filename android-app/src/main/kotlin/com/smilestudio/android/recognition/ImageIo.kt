package com.smilestudio.android.recognition

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun createCaptureImageUri(context: Context): Uri {
    val file = File(context.cacheDir, "captured_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}

fun readImageBytes(context: Context, uri: Uri): ByteArray? =
    context.contentResolver.openInputStream(uri)?.use { it.readBytes() }

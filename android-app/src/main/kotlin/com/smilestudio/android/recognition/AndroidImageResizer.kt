package com.smilestudio.android.recognition

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

private const val MAX_DIMENSION_PX = 1024
private const val JPEG_QUALITY = 80

object AndroidImageResizer {
    fun resize(imageBytes: ByteArray): ByteArray {
        val original = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            ?: return imageBytes
        val scale = MAX_DIMENSION_PX.toFloat() / maxOf(original.width, original.height)
        val scaled = if (scale < 1f) {
            Bitmap.createScaledBitmap(
                original,
                (original.width * scale).toInt().coerceAtLeast(1),
                (original.height * scale).toInt().coerceAtLeast(1),
                true,
            )
        } else {
            original
        }
        return ByteArrayOutputStream().use { stream ->
            scaled.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, stream)
            stream.toByteArray()
        }
    }
}

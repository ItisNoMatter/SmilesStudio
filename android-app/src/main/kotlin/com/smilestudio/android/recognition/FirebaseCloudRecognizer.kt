package com.smilestudio.android.recognition

import android.util.Base64
import com.google.firebase.Firebase
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.functions
import kotlinx.coroutines.tasks.await

private const val RECOGNIZE_IMAGE_FUNCTION_NAME = "recognizeImage"
private const val IMAGE_MIME_TYPE = "image/jpeg"
private const val RECOGNITION_FAILURE_MESSAGE = "画像から構造式を認識できませんでした"

/**
 * Imperative shell around the `recognizeImage` Callable Function (Issue #34) for the non-BYOK
 * path (AnyDR 0121, 0133, 0134). Untested per this project's functional-core/imperative-shell
 * convention -- verified via emulator instead, same as [com.smilestudio.android.recognition.AndroidImageResizer].
 */
object FirebaseCloudRecognizer {
    suspend fun recognize(imageBytes: ByteArray): CloudRecognitionResult {
        val request = mapOf(
            "imageBase64" to Base64.encodeToString(imageBytes, Base64.NO_WRAP),
            "mimeType" to IMAGE_MIME_TYPE,
        )
        return try {
            val result = Firebase.functions
                .getHttpsCallable(RECOGNIZE_IMAGE_FUNCTION_NAME)
                .call(request)
                .await()
            val response = result.data as? Map<*, *>
            val smiles = response?.get("smiles") as? String
            if (smiles == null) {
                CloudRecognitionResult.Failure(RECOGNITION_FAILURE_MESSAGE)
            } else {
                val remainingFreeCount = (response["remainingFreeCount"] as? Number)?.toInt()
                CloudRecognitionResult.Success(smiles, remainingFreeCount)
            }
        } catch (e: FirebaseFunctionsException) {
            if (e.code == FirebaseFunctionsException.Code.RESOURCE_EXHAUSTED) {
                CloudRecognitionResult.FreeTierExhausted
            } else {
                CloudRecognitionResult.Failure(e.message ?: RECOGNITION_FAILURE_MESSAGE)
            }
        }
    }
}

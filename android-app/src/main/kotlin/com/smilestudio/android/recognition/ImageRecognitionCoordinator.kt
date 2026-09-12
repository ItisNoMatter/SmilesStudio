package com.smilestudio.android.recognition

import com.smilestudio.vision.LLMProvider
import com.smilestudio.vision.RecognitionResult

class ImageRecognitionCoordinator(
    private val getApiKey: () -> String?,
    private val resizeImage: (ByteArray) -> ByteArray,
    private val recognize: suspend (ByteArray, String, LLMProvider) -> RecognitionResult,
) {
    suspend fun recognize(imageBytes: ByteArray): ImageRecognitionOutcome {
        val apiKey = getApiKey() ?: return ImageRecognitionOutcome.MissingApiKey
        val resized = resizeImage(imageBytes)
        return when (val result = recognize(resized, apiKey, LLMProvider.GOOGLE_GEMINI)) {
            is RecognitionResult.Success -> ImageRecognitionOutcome.Recognized(result.smiles)
            is RecognitionResult.Failure -> ImageRecognitionOutcome.Failed(result.reason)
        }
    }
}

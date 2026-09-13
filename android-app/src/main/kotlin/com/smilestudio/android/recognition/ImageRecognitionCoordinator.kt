package com.smilestudio.android.recognition

import com.smilestudio.vision.LLMProvider
import com.smilestudio.vision.RecognitionResult

class ImageRecognitionCoordinator(
    private val getApiKey: () -> String?,
    private val resizeImage: (ByteArray) -> ByteArray,
    private val recognize: suspend (ByteArray, String, LLMProvider) -> RecognitionResult,
    private val recognizeViaCloud: suspend (ByteArray) -> CloudRecognitionResult,
) {
    suspend fun recognize(imageBytes: ByteArray): ImageRecognitionOutcome {
        val resized = resizeImage(imageBytes)
        val apiKey = getApiKey()
        return if (apiKey != null) {
            recognizeViaByok(resized, apiKey)
        } else {
            recognizeViaCloudFunction(resized)
        }
    }

    private suspend fun recognizeViaByok(resized: ByteArray, apiKey: String): ImageRecognitionOutcome {
        return when (val result = recognize(resized, apiKey, LLMProvider.GOOGLE_GEMINI)) {
            is RecognitionResult.Success -> ImageRecognitionOutcome.Recognized(result.smiles)
            is RecognitionResult.Failure -> ImageRecognitionOutcome.Failed(result.reason)
        }
    }

    private suspend fun recognizeViaCloudFunction(resized: ByteArray): ImageRecognitionOutcome {
        return when (val result = recognizeViaCloud(resized)) {
            is CloudRecognitionResult.Success ->
                ImageRecognitionOutcome.Recognized(result.smiles, result.remainingFreeCount)
            CloudRecognitionResult.FreeTierExhausted -> ImageRecognitionOutcome.FreeTierExhausted
            is CloudRecognitionResult.Failure -> ImageRecognitionOutcome.Failed(result.reason)
        }
    }
}

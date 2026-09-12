package com.smilestudio.android.recognition

import com.smilestudio.vision.LLMProvider
import com.smilestudio.vision.RecognitionResult
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ImageRecognitionCoordinatorTest {
    @Test
    fun `returns MissingApiKey when no api key is stored`() = runBlocking {
        var recognizeCalled = false
        val coordinator = ImageRecognitionCoordinator(
            getApiKey = { null },
            resizeImage = { it },
            recognize = { _, _, _ ->
                recognizeCalled = true
                RecognitionResult.Success("C")
            },
        )

        val outcome = coordinator.recognize(byteArrayOf(1, 2, 3))

        assertIs<ImageRecognitionOutcome.MissingApiKey>(outcome)
        assertEquals(false, recognizeCalled)
    }

    @Test
    fun `resizes the image and returns Recognized on success`() = runBlocking {
        val resized = byteArrayOf(9, 9)
        var receivedImage: ByteArray? = null
        var receivedApiKey: String? = null
        var receivedProvider: LLMProvider? = null
        val coordinator = ImageRecognitionCoordinator(
            getApiKey = { "test-api-key" },
            resizeImage = { resized },
            recognize = { image, apiKey, provider ->
                receivedImage = image
                receivedApiKey = apiKey
                receivedProvider = provider
                RecognitionResult.Success("c1ccccc1")
            },
        )

        val outcome = coordinator.recognize(byteArrayOf(1, 2, 3))

        assertEquals(ImageRecognitionOutcome.Recognized("c1ccccc1"), outcome)
        assertEquals(resized, receivedImage)
        assertEquals("test-api-key", receivedApiKey)
        assertEquals(LLMProvider.GOOGLE_GEMINI, receivedProvider)
    }

    @Test
    fun `returns Failed with the reason when recognition fails`() = runBlocking {
        val coordinator = ImageRecognitionCoordinator(
            getApiKey = { "test-api-key" },
            resizeImage = { it },
            recognize = { _, _, _ -> RecognitionResult.Failure("network error") },
        )

        val outcome = coordinator.recognize(byteArrayOf(1, 2, 3))

        assertEquals(ImageRecognitionOutcome.Failed("network error"), outcome)
    }
}

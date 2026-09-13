package com.smilestudio.android.recognition

import com.smilestudio.vision.LLMProvider
import com.smilestudio.vision.RecognitionResult
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class ImageRecognitionCoordinatorTest {
    @Test
    fun `resizes the image and returns Recognized on BYOK success`() = runBlocking {
        val resized = byteArrayOf(9, 9)
        var receivedImage: ByteArray? = null
        var receivedApiKey: String? = null
        var receivedProvider: LLMProvider? = null
        var cloudCalled = false
        val coordinator = ImageRecognitionCoordinator(
            getApiKey = { "test-api-key" },
            resizeImage = { resized },
            recognize = { image, apiKey, provider ->
                receivedImage = image
                receivedApiKey = apiKey
                receivedProvider = provider
                RecognitionResult.Success("c1ccccc1")
            },
            recognizeViaCloud = {
                cloudCalled = true
                CloudRecognitionResult.Success("should-not-be-used", remainingFreeCount = null)
            },
        )

        val outcome = coordinator.recognize(byteArrayOf(1, 2, 3))

        assertEquals(ImageRecognitionOutcome.Recognized("c1ccccc1"), outcome)
        assertEquals(resized, receivedImage)
        assertEquals("test-api-key", receivedApiKey)
        assertEquals(LLMProvider.GOOGLE_GEMINI, receivedProvider)
        assertEquals(false, cloudCalled)
    }

    @Test
    fun `returns Failed with the reason when BYOK recognition fails`() = runBlocking {
        val coordinator = ImageRecognitionCoordinator(
            getApiKey = { "test-api-key" },
            resizeImage = { it },
            recognize = { _, _, _ -> RecognitionResult.Failure("network error") },
            recognizeViaCloud = { CloudRecognitionResult.Success("unused", null) },
        )

        val outcome = coordinator.recognize(byteArrayOf(1, 2, 3))

        assertEquals(ImageRecognitionOutcome.Failed("network error"), outcome)
    }

    @Test
    fun `uses the cloud path and returns Recognized with remaining count when no api key is stored`() = runBlocking {
        val resized = byteArrayOf(9, 9)
        var receivedImage: ByteArray? = null
        var byokCalled = false
        val coordinator = ImageRecognitionCoordinator(
            getApiKey = { null },
            resizeImage = { resized },
            recognize = { _, _, _ ->
                byokCalled = true
                RecognitionResult.Success("should-not-be-used")
            },
            recognizeViaCloud = { image ->
                receivedImage = image
                CloudRecognitionResult.Success("c1ccccc1", remainingFreeCount = 3)
            },
        )

        val outcome = coordinator.recognize(byteArrayOf(1, 2, 3))

        assertEquals(ImageRecognitionOutcome.Recognized("c1ccccc1", remainingFreeCount = 3), outcome)
        assertEquals(resized, receivedImage)
        assertEquals(false, byokCalled)
    }

    @Test
    fun `returns FreeTierExhausted via the cloud path`() = runBlocking {
        val coordinator = ImageRecognitionCoordinator(
            getApiKey = { null },
            resizeImage = { it },
            recognize = { _, _, _ -> RecognitionResult.Success("unused") },
            recognizeViaCloud = { CloudRecognitionResult.FreeTierExhausted },
        )

        val outcome = coordinator.recognize(byteArrayOf(1, 2, 3))

        assertEquals(ImageRecognitionOutcome.FreeTierExhausted, outcome)
    }

    @Test
    fun `returns Failed with the reason when the cloud path fails`() = runBlocking {
        val coordinator = ImageRecognitionCoordinator(
            getApiKey = { null },
            resizeImage = { it },
            recognize = { _, _, _ -> RecognitionResult.Success("unused") },
            recognizeViaCloud = { CloudRecognitionResult.Failure("server error") },
        )

        val outcome = coordinator.recognize(byteArrayOf(1, 2, 3))

        assertEquals(ImageRecognitionOutcome.Failed("server error"), outcome)
    }
}

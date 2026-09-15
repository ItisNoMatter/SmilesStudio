package com.smilestudio.android

import com.smilestudio.android.apikey.ApiKeyEncryptor
import com.smilestudio.android.apikey.ApiKeyStore
import com.smilestudio.android.apikey.EncryptedApiKey
import com.smilestudio.android.apikey.KeyValueStore
import com.smilestudio.android.recognition.CloudRecognitionResult
import com.smilestudio.android.recognition.ImageRecognitionCoordinator
import com.smilestudio.android.recognition.ImageRecognitionOutcome
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

private class FakeApiKeyEncryptor : ApiKeyEncryptor {
    override fun encrypt(plainText: String): EncryptedApiKey =
        EncryptedApiKey(ciphertext = plainText.reversed(), iv = "fake-iv")

    override fun decrypt(encrypted: EncryptedApiKey): String = encrypted.ciphertext.reversed()
}

private class FakeKeyValueStore : KeyValueStore {
    private val values = mutableMapOf<String, String>()

    override fun getString(key: String): String? = values[key]
    override fun putString(key: String, value: String) {
        values[key] = value
    }
    override fun remove(key: String) {
        values.remove(key)
    }
}

private fun newState(
    apiKeyStore: ApiKeyStore = ApiKeyStore(FakeApiKeyEncryptor(), FakeKeyValueStore()),
    recognize: suspend (ByteArray) -> ImageRecognitionOutcome = { error("not stubbed") },
    showSnackbar: suspend (String) -> Unit = {},
): SmilesStudioAppState {
    val coordinator = ImageRecognitionCoordinator(
        getApiKey = { null },
        resizeImage = { it },
        recognize = { _, _, _ -> error("BYOK path not used in these tests") },
        recognizeViaCloud = { image ->
            when (val outcome = recognize(image)) {
                is ImageRecognitionOutcome.Recognized ->
                    CloudRecognitionResult.Success(outcome.smiles, outcome.remainingFreeCount)
                is ImageRecognitionOutcome.Failed -> CloudRecognitionResult.Failure(outcome.reason)
                ImageRecognitionOutcome.FreeTierExhausted -> CloudRecognitionResult.FreeTierExhausted
            }
        },
    )
    return SmilesStudioAppState(
        apiKeyStore = apiKeyStore,
        recognitionCoordinator = coordinator,
        showSnackbar = showSnackbar,
    )
}

class SmilesStudioAppStateTest {
    @Test
    fun `initial tab is HOME`() {
        val state = newState()

        assertEquals(AppTab.HOME, state.selectedTab)
    }

    @Test
    fun `currentApiKey reflects the value already stored`() {
        val apiKeyStore = ApiKeyStore(FakeApiKeyEncryptor(), FakeKeyValueStore())
        apiKeyStore.save("stored-key")

        val state = newState(apiKeyStore = apiKeyStore)

        assertEquals("stored-key", state.currentApiKey)
    }

    @Test
    fun `saveApiKey persists the key and updates currentApiKey`() {
        val state = newState()

        state.saveApiKey("new-key")

        assertEquals("new-key", state.currentApiKey)
    }

    @Test
    fun `deleteApiKey clears currentApiKey`() {
        val apiKeyStore = ApiKeyStore(FakeApiKeyEncryptor(), FakeKeyValueStore())
        apiKeyStore.save("stored-key")
        val state = newState(apiKeyStore = apiKeyStore)

        state.deleteApiKey()

        assertNull(state.currentApiKey)
    }

    @Test
    fun `runRecognition shows a snackbar and does not touch smilesText when image bytes are null`() = runBlocking {
        var snackbarMessage: String? = null
        val state = newState(showSnackbar = { snackbarMessage = it })

        state.runRecognition(imageBytes = null)

        assertEquals("画像の読み込みに失敗しました", snackbarMessage)
        assertEquals("", state.smilesText)
        assertFalse(state.isRecognizing)
    }

    @Test
    fun `runRecognition updates smilesText on success without remaining count`() = runBlocking {
        var snackbarCalled = false
        val state = newState(
            recognize = { ImageRecognitionOutcome.Recognized("c1ccccc1") },
            showSnackbar = { snackbarCalled = true },
        )

        state.runRecognition(imageBytes = byteArrayOf(1, 2, 3))

        assertEquals("c1ccccc1", state.smilesText)
        assertFalse(snackbarCalled)
        assertFalse(state.isRecognizing)
    }

    @Test
    fun `runRecognition shows the remaining free count when present`() = runBlocking {
        var snackbarMessage: String? = null
        val state = newState(
            recognize = { ImageRecognitionOutcome.Recognized("c1ccccc1", remainingFreeCount = 3) },
            showSnackbar = { snackbarMessage = it },
        )

        state.runRecognition(imageBytes = byteArrayOf(1, 2, 3))

        assertEquals("c1ccccc1", state.smilesText)
        assertEquals("あと3回無料でご利用いただけます", snackbarMessage)
    }

    @Test
    fun `runRecognition shows the failure reason and leaves smilesText untouched`() = runBlocking {
        var snackbarMessage: String? = null
        val state = newState(
            recognize = { ImageRecognitionOutcome.Failed("network error") },
            showSnackbar = { snackbarMessage = it },
        )

        state.runRecognition(imageBytes = byteArrayOf(1, 2, 3))

        assertEquals("network error", snackbarMessage)
        assertEquals("", state.smilesText)
    }

    @Test
    fun `runRecognition opens the paywall when the free tier is exhausted`() = runBlocking {
        val state = newState(recognize = { ImageRecognitionOutcome.FreeTierExhausted })

        state.runRecognition(imageBytes = byteArrayOf(1, 2, 3))

        assertTrue(state.showPaywall)
    }
}

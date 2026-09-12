package com.smilestudio.android.apikey

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

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

class ApiKeyStoreTest {
    @Test
    fun `save then get returns the same api key`() {
        val store = ApiKeyStore(FakeApiKeyEncryptor(), FakeKeyValueStore())

        store.save("test-api-key-123")

        assertEquals("test-api-key-123", store.get())
    }

    @Test
    fun `get returns null when nothing is saved`() {
        val store = ApiKeyStore(FakeApiKeyEncryptor(), FakeKeyValueStore())

        assertNull(store.get())
    }

    @Test
    fun `save rejects a blank api key and keeps the existing one`() {
        val store = ApiKeyStore(FakeApiKeyEncryptor(), FakeKeyValueStore())
        store.save("existing-key")

        val saved = store.save("   ")

        assertFalse(saved)
        assertEquals("existing-key", store.get())
    }

    @Test
    fun `delete removes the saved key`() {
        val store = ApiKeyStore(FakeApiKeyEncryptor(), FakeKeyValueStore())
        store.save("test-api-key-123")

        store.delete()

        assertNull(store.get())
    }
}

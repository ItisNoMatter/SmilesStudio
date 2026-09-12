package com.smilestudio.android.apikey

private const val KEY_CIPHERTEXT = "api_key_ciphertext"
private const val KEY_IV = "api_key_iv"

class ApiKeyStore(private val encryptor: ApiKeyEncryptor, private val store: KeyValueStore) {
    fun save(apiKey: String): Boolean {
        if (apiKey.isBlank()) return false
        val encrypted = encryptor.encrypt(apiKey)
        store.putString(KEY_CIPHERTEXT, encrypted.ciphertext)
        store.putString(KEY_IV, encrypted.iv)
        return true
    }

    fun get(): String? {
        val ciphertext = store.getString(KEY_CIPHERTEXT) ?: return null
        val iv = store.getString(KEY_IV) ?: return null
        return encryptor.decrypt(EncryptedApiKey(ciphertext, iv))
    }

    fun delete() {
        store.remove(KEY_CIPHERTEXT)
        store.remove(KEY_IV)
    }
}

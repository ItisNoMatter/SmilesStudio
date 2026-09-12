package com.smilestudio.android.apikey

data class EncryptedApiKey(val ciphertext: String, val iv: String)

interface ApiKeyEncryptor {
    fun encrypt(plainText: String): EncryptedApiKey
    fun decrypt(encrypted: EncryptedApiKey): String
}

package com.smilestudio.vision

import ai.koog.http.client.ktor.KtorKoogHttpClient
import ai.koog.prompt.executor.clients.LLMClient
import ai.koog.prompt.executor.clients.google.GoogleLLMClient
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.MultiLLMPromptExecutor
import ai.koog.prompt.llm.LLModel

suspend fun recognizeStructure(imageBytes: ByteArray, apiKey: String, provider: LLMProvider): RecognitionResult {
    val client: LLMClient
    val model: LLModel
    when (provider) {
        LLMProvider.GOOGLE_GEMINI -> {
            client = GoogleLLMClient(apiKey, httpClientFactory = KtorKoogHttpClient.Factory())
            model = GoogleModels.Gemini2_5Flash
        }
    }
    return runRecognition(MultiLLMPromptExecutor(client), model, imageBytes)
}

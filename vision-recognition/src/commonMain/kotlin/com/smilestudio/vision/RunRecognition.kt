package com.smilestudio.vision

import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.model.PromptExecutor
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.message.AttachmentContent
import ai.koog.prompt.message.AttachmentSource
import kotlinx.coroutines.CancellationException

private const val RECOGNITION_INSTRUCTION =
    "Identify the chemical structure drawn in this image and respond with ONLY its SMILES string, nothing else."

internal const val RATE_LIMIT_FAILURE_REASON =
    "APIの利用上限（レート制限）に達しました。しばらく待ってから再試行してください。"

private val RATE_LIMIT_MARKERS = listOf("429", "resource_exhausted", "rate limit", "too many requests", "quota")

private fun isRateLimitError(message: String): Boolean {
    val lower = message.lowercase()
    return RATE_LIMIT_MARKERS.any { lower.contains(it) }
}

suspend fun runRecognition(promptExecutor: PromptExecutor, model: LLModel, imageBytes: ByteArray): RecognitionResult {
    val recognitionPrompt = prompt("vision-recognition") {
        user {
            +RECOGNITION_INSTRUCTION
            image(
                AttachmentSource.Image(
                    content = AttachmentContent.Binary.Bytes(imageBytes),
                    format = "png",
                    mimeType = "image/png",
                    fileName = "structure.png",
                ),
            )
        }
    }

    val response = try {
        promptExecutor.execute(recognitionPrompt, model)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        val message = e.message ?: e.toString()
        return if (isRateLimitError(message)) {
            RecognitionResult.Failure(RATE_LIMIT_FAILURE_REASON)
        } else {
            RecognitionResult.Failure(message)
        }
    }
    val text = response.textContent().trim()
    return if (text.isBlank()) {
        RecognitionResult.Failure("LLM returned an empty response")
    } else {
        RecognitionResult.Success(text)
    }
}

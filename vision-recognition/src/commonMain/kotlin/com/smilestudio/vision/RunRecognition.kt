package com.smilestudio.vision

import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.model.PromptExecutor
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.message.AttachmentContent
import ai.koog.prompt.message.AttachmentSource

private const val RECOGNITION_INSTRUCTION =
    "Identify the chemical structure drawn in this image and respond with ONLY its SMILES string, nothing else."

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

    val response = promptExecutor.execute(recognitionPrompt, model)
    val text = response.textContent().trim()
    return if (text.isBlank()) {
        RecognitionResult.Failure("LLM returned an empty response")
    } else {
        RecognitionResult.Success(text)
    }
}

package com.smilestudio.vision

import ai.koog.agents.core.tools.ToolDescriptor
import ai.koog.agents.testing.tools.getMockExecutor
import ai.koog.prompt.Prompt
import ai.koog.prompt.dsl.ModerationResult
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.model.PromptExecutor
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.message.Message
import ai.koog.prompt.streaming.StreamFrame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

private class ThrowingPromptExecutor(private val exception: Throwable) : PromptExecutor() {
    override suspend fun execute(prompt: Prompt, model: LLModel, tools: List<ToolDescriptor>): Message.Assistant =
        throw exception

    override fun executeStreaming(prompt: Prompt, model: LLModel, tools: List<ToolDescriptor>): Flow<StreamFrame> =
        throw exception

    override suspend fun moderate(prompt: Prompt, model: LLModel): ModerationResult = throw exception

    override fun close() = Unit
}

class RunRecognitionTest {

    @Test
    fun `LLMが返したテキストがSMILESとして成功結果になる`() = runBlocking {
        val mockExecutor = getMockExecutor {
            mockLLMAnswer("CCO").asDefaultResponse
        }

        val result = runRecognition(mockExecutor, GoogleModels.Gemini2_5Flash, imageBytes = byteArrayOf(1, 2, 3))

        assertIs<RecognitionResult.Success>(result)
        assertEquals("CCO", result.smiles)
    }

    @Test
    fun `LLMの応答テキストの前後の空白は除去される`() = runBlocking {
        val mockExecutor = getMockExecutor {
            mockLLMAnswer("  c1ccccc1  \n").asDefaultResponse
        }

        val result = runRecognition(mockExecutor, GoogleModels.Gemini2_5Flash, imageBytes = byteArrayOf(1, 2, 3))

        assertIs<RecognitionResult.Success>(result)
        assertEquals("c1ccccc1", result.smiles)
    }

    @Test
    fun `LLM呼び出しが例外を投げた場合はFailureになる`() = runBlocking {
        val executor = ThrowingPromptExecutor(IllegalStateException("network error"))

        val result = runRecognition(executor, GoogleModels.Gemini2_5Flash, imageBytes = byteArrayOf(1, 2, 3))

        assertIs<RecognitionResult.Failure>(result)
        assertEquals("network error", result.reason)
    }

    @Test
    fun `例外メッセージに429が含まれる場合はレート制限のFailureになる`() = runBlocking {
        val executor = ThrowingPromptExecutor(
            IllegalStateException("Client request invalid: 429 Too Many Requests"),
        )

        val result = runRecognition(executor, GoogleModels.Gemini2_5Flash, imageBytes = byteArrayOf(1, 2, 3))

        assertIs<RecognitionResult.Failure>(result)
        assertEquals(RATE_LIMIT_FAILURE_REASON, result.reason)
    }

    @Test
    fun `例外メッセージにRESOURCE_EXHAUSTEDが含まれる場合はレート制限のFailureになる`() = runBlocking {
        val executor = ThrowingPromptExecutor(
            IllegalStateException("status: RESOURCE_EXHAUSTED, message: Quota exceeded"),
        )

        val result = runRecognition(executor, GoogleModels.Gemini2_5Flash, imageBytes = byteArrayOf(1, 2, 3))

        assertIs<RecognitionResult.Failure>(result)
        assertEquals(RATE_LIMIT_FAILURE_REASON, result.reason)
    }
}

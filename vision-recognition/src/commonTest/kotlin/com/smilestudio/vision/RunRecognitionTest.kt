package com.smilestudio.vision

import ai.koog.agents.testing.tools.getMockExecutor
import ai.koog.prompt.executor.clients.google.GoogleModels
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

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
}

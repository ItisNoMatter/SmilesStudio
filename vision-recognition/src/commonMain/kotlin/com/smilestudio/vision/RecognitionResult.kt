package com.smilestudio.vision

sealed class RecognitionResult {
    data class Success(val smiles: String) : RecognitionResult()
    data class Failure(val reason: String) : RecognitionResult()
}

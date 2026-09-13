package com.smilestudio.android.recognition

sealed class CloudRecognitionResult {
    data class Success(val smiles: String, val remainingFreeCount: Int?) : CloudRecognitionResult()
    object FreeTierExhausted : CloudRecognitionResult()
    data class Failure(val reason: String) : CloudRecognitionResult()
}

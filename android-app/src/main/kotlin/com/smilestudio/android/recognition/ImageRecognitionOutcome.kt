package com.smilestudio.android.recognition

sealed class ImageRecognitionOutcome {
    data class Recognized(val smiles: String, val remainingFreeCount: Int? = null) : ImageRecognitionOutcome()
    data class Failed(val reason: String) : ImageRecognitionOutcome()
    object FreeTierExhausted : ImageRecognitionOutcome()
}

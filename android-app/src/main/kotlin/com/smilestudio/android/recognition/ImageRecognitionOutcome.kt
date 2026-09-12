package com.smilestudio.android.recognition

sealed class ImageRecognitionOutcome {
    data class Recognized(val smiles: String) : ImageRecognitionOutcome()
    data class Failed(val reason: String) : ImageRecognitionOutcome()
    object MissingApiKey : ImageRecognitionOutcome()
}

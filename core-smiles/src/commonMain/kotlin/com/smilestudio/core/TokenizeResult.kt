package com.smilestudio.core

sealed class TokenizeResult {
    data class Success(val tokens: List<PositionedToken>) : TokenizeResult()
    data class Failure(val reason: String) : TokenizeResult()
}

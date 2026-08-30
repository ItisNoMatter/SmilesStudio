package com.smilestudio.core

sealed class ParseResult {
    data class Success(val molecule: Molecule) : ParseResult()
    data class Failure(val reason: String) : ParseResult()
}

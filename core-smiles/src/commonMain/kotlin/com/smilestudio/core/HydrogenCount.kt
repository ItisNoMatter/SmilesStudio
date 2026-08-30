package com.smilestudio.core

sealed interface HydrogenCount {
    data object Implicit : HydrogenCount
    data class Explicit(val count: Int) : HydrogenCount
}

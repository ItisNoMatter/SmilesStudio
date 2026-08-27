package com.smilestudio.core

data class Atom(
    val id: AtomId,
    val element: Element,
    val charge: Int = 0,
    val isotope: Int? = null,
    val isAromatic: Boolean = false,
    val hydrogenCount: Int? = null,
)

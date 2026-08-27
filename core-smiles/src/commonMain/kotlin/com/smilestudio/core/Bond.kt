package com.smilestudio.core

data class Bond(
    val atom1: AtomId,
    val atom2: AtomId,
    val type: BondType,
)

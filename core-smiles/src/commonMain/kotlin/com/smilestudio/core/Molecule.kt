package com.smilestudio.core

data class Molecule(
    val atoms: Map<AtomId, Atom>,
    val bonds: List<Bond>,
)

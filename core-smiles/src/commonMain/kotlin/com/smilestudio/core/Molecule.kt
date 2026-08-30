package com.smilestudio.core

data class Molecule(
    val atoms: Map<AtomId, Atom>,
    val bonds: List<Bond>,
) {
    private val bondsByAtom: Map<AtomId, List<Bond>> by lazy {
        buildMap<AtomId, MutableList<Bond>> {
            for (bond in bonds) {
                getOrPut(bond.atom1) { mutableListOf() }.add(bond)
                getOrPut(bond.atom2) { mutableListOf() }.add(bond)
            }
        }
    }

    private val aromaticAtomIds: Set<AtomId> by lazy {
        atoms.keys.filterTo(mutableSetOf()) { atomId ->
            val adjacentBonds = bondsByAtom[atomId]
            !adjacentBonds.isNullOrEmpty() && adjacentBonds.all { it.type == BondType.AROMATIC }
        }
    }

    fun isAromatic(atomId: AtomId): Boolean = atomId in aromaticAtomIds
}

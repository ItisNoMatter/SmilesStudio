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

    val rings: List<Ring> by lazy { findRings() }

    private fun findRings(): List<Ring> {
        val visited = mutableSetOf<AtomId>()
        val parent = mutableMapOf<AtomId, AtomId>()
        val discoveryOrder = mutableMapOf<AtomId, Int>()
        val rings = mutableListOf<Ring>()
        var clock = 0

        fun visit(atomId: AtomId, parentBond: Bond?) {
            visited += atomId
            discoveryOrder[atomId] = clock++
            for (bond in bondsByAtom[atomId].orEmpty()) {
                if (bond === parentBond) continue
                val neighbor = if (bond.atom1 == atomId) bond.atom2 else bond.atom1
                if (neighbor !in visited) {
                    parent[neighbor] = atomId
                    visit(neighbor, bond)
                } else if (discoveryOrder.getValue(neighbor) < discoveryOrder.getValue(atomId)) {
                    val cycle = mutableListOf(atomId)
                    var current = atomId
                    while (current != neighbor) {
                        current = parent.getValue(current)
                        cycle += current
                    }
                    rings += Ring(cycle.asReversed())
                }
            }
        }

        for (atomId in atoms.keys) {
            if (atomId !in visited) {
                visit(atomId, parentBond = null)
            }
        }
        return rings
    }
}

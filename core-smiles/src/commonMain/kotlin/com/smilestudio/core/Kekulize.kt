package com.smilestudio.core

fun kekulize(molecule: Molecule): List<Bond> {
    val resolvedTypes = mutableMapOf<Bond, BondType>()
    for (ring in molecule.rings) {
        val bonds = ringBonds(ring, molecule)
        if (bonds.any { it.type != BondType.AROMATIC }) continue
        bonds.forEachIndexed { index, bond ->
            resolvedTypes[bond] = if (index % 2 == 0) BondType.DOUBLE else BondType.SINGLE
        }
    }
    return molecule.bonds.map { bond -> resolvedTypes[bond]?.let { bond.copy(type = it) } ?: bond }
}

private fun ringBonds(ring: Ring, molecule: Molecule): List<Bond> {
    val atoms = ring.atoms
    return atoms.indices.map { i ->
        val a = atoms[i]
        val b = atoms[(i + 1) % atoms.size]
        molecule.bonds.first { (it.atom1 == a && it.atom2 == b) || (it.atom1 == b && it.atom2 == a) }
    }
}

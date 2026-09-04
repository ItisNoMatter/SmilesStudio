package com.smilestudio.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private fun bondBetween(bonds: List<Bond>, a: AtomId, b: AtomId): Bond =
    bonds.first { (it.atom1 == a && it.atom2 == b) || (it.atom1 == b && it.atom2 == a) }

class KekulizeTest {

    @Test
    fun `芳香族結合を持たない分子はそのまま返される`() {
        val c0 = AtomId(0)
        val c1 = AtomId(1)
        val molecule = Molecule(
            atoms = mapOf(c0 to Atom(c0, Element.C), c1 to Atom(c1, Element.C)),
            bonds = listOf(Bond(c0, c1, BondType.SINGLE)),
        )

        val result = kekulize(molecule)

        assertEquals(molecule.bonds, result)
    }

    @Test
    fun `ベンゼンの芳香族結合は単結合と二重結合の交互パターンになる`() {
        val atomIds = (0..5).map { AtomId(it) }
        val atoms = atomIds.associateWith { Atom(it, Element.C) }
        val bonds = (0..4).map { i -> Bond(atomIds[i], atomIds[i + 1], BondType.AROMATIC) } +
            Bond(atomIds[5], atomIds[0], BondType.AROMATIC)
        val molecule = Molecule(atoms = atoms, bonds = bonds)

        val result = kekulize(molecule)

        assertEquals(3, result.count { it.type == BondType.DOUBLE }, "should have 3 double bonds")
        assertEquals(3, result.count { it.type == BondType.SINGLE }, "should have 3 single bonds")
        assertTrue(result.none { it.type == BondType.AROMATIC }, "no AROMATIC bond should remain")
    }

    @Test
    fun `ベンゼンの環内で隣接する2つの結合が両方二重結合になることはない`() {
        val atomIds = (0..5).map { AtomId(it) }
        val atoms = atomIds.associateWith { Atom(it, Element.C) }
        val bonds = (0..4).map { i -> Bond(atomIds[i], atomIds[i + 1], BondType.AROMATIC) } +
            Bond(atomIds[5], atomIds[0], BondType.AROMATIC)
        val molecule = Molecule(atoms = atoms, bonds = bonds)

        val result = kekulize(molecule)

        for (i in 0..5) {
            val current = bondBetween(result, atomIds[i], atomIds[(i + 1) % 6])
            val next = bondBetween(result, atomIds[(i + 1) % 6], atomIds[(i + 2) % 6])
            assertTrue(
                !(current.type == BondType.DOUBLE && next.type == BondType.DOUBLE),
                "adjacent ring bonds at index $i must not both be DOUBLE",
            )
        }
    }

    @Test
    fun `環に結合した非芳香族の置換基は変化しない`() {
        // Toluene-like: an aromatic ring (0..5) with a non-aromatic methyl substituent (6).
        val ringAtomIds = (0..5).map { AtomId(it) }
        val substituent = AtomId(6)
        val atoms = (ringAtomIds + substituent).associateWith { Atom(it, Element.C) }
        val ringBonds = (0..4).map { i -> Bond(ringAtomIds[i], ringAtomIds[i + 1], BondType.AROMATIC) } +
            Bond(ringAtomIds[5], ringAtomIds[0], BondType.AROMATIC)
        val substituentBond = Bond(ringAtomIds[0], substituent, BondType.SINGLE)
        val molecule = Molecule(atoms = atoms, bonds = ringBonds + substituentBond)

        val result = kekulize(molecule)

        val resultSubstituentBond = bondBetween(result, ringAtomIds[0], substituent)
        assertEquals(BondType.SINGLE, resultSubstituentBond.type)
    }

    @Test
    fun `窒素を含む芳香環（ピリジン）でも同様に交互パターンになる`() {
        val n0 = AtomId(0)
        val atomIds = listOf(n0) + (1..5).map { AtomId(it) }
        val atoms = atomIds.associateWith { id -> Atom(id, if (id == n0) Element.N else Element.C) }
        val bonds = (0..4).map { i -> Bond(atomIds[i], atomIds[i + 1], BondType.AROMATIC) } +
            Bond(atomIds[5], atomIds[0], BondType.AROMATIC)
        val molecule = Molecule(atoms = atoms, bonds = bonds)

        val result = kekulize(molecule)

        assertEquals(3, result.count { it.type == BondType.DOUBLE }, "should have 3 double bonds")
        assertEquals(3, result.count { it.type == BondType.SINGLE }, "should have 3 single bonds")
    }
}

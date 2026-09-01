package com.smilestudio.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MoleculeTest {

    @Test
    fun `分子はAtomIdを介して原子と結合を保持する`() {
        val carbon = AtomId(0)
        val oxygen = AtomId(1)

        val molecule = Molecule(
            atoms = mapOf(
                carbon to Atom(id = carbon, element = Element.C),
                oxygen to Atom(id = oxygen, element = Element.O),
            ),
            bonds = listOf(
                Bond(atom1 = carbon, atom2 = oxygen, type = BondType.DOUBLE),
            ),
        )

        assertEquals(2, molecule.atoms.size)
        assertEquals(Element.O, molecule.atoms.getValue(oxygen).element)
        assertEquals(BondType.DOUBLE, molecule.bonds.single().type)
        assertTrue(molecule.bonds.single().atom1 == carbon)
    }

    @Test
    fun `原子のデフォルト値は暗黙的な水素数を持つ中性原子を表す`() {
        val atom = Atom(id = AtomId(0), element = Element.N)

        assertEquals(0, atom.charge)
        assertEquals(null, atom.isotope)
        assertEquals(HydrogenCount.Implicit, atom.hydrogenCount)
    }

    @Test
    fun `明示的な水素数は角括弧記法で指定された数を保持する`() {
        val atom = Atom(id = AtomId(0), element = Element.C, hydrogenCount = HydrogenCount.Explicit(3))

        assertEquals(HydrogenCount.Explicit(3), atom.hydrogenCount)
    }

    @Test
    fun `隣接する結合がすべて芳香族結合なら原子は芳香族である`() {
        val c1 = AtomId(0)
        val c2 = AtomId(1)
        val molecule = Molecule(
            atoms = mapOf(
                c1 to Atom(id = c1, element = Element.C),
                c2 to Atom(id = c2, element = Element.C),
            ),
            bonds = listOf(
                Bond(atom1 = c1, atom2 = c2, type = BondType.AROMATIC),
            ),
        )

        assertTrue(molecule.isAromatic(c1))
        assertTrue(molecule.isAromatic(c2))
    }

    @Test
    fun `隣接する結合に非芳香族結合が含まれる原子は芳香族ではない`() {
        val c1 = AtomId(0)
        val c2 = AtomId(1)
        val c3 = AtomId(2)
        val molecule = Molecule(
            atoms = mapOf(
                c1 to Atom(id = c1, element = Element.C),
                c2 to Atom(id = c2, element = Element.C),
                c3 to Atom(id = c3, element = Element.C),
            ),
            bonds = listOf(
                Bond(atom1 = c1, atom2 = c2, type = BondType.AROMATIC),
                Bond(atom1 = c1, atom2 = c3, type = BondType.SINGLE),
            ),
        )

        assertFalse(molecule.isAromatic(c1))
    }

    @Test
    fun `結合を持たない原子は芳香族ではない`() {
        val c1 = AtomId(0)
        val molecule = Molecule(
            atoms = mapOf(c1 to Atom(id = c1, element = Element.C)),
            bonds = emptyList(),
        )

        assertFalse(molecule.isAromatic(c1))
    }

    @Test
    fun `環を持たない直鎖分子のringsは空になる`() {
        val c0 = AtomId(0)
        val c1 = AtomId(1)
        val o2 = AtomId(2)
        val molecule = Molecule(
            atoms = mapOf(
                c0 to Atom(id = c0, element = Element.C),
                c1 to Atom(id = c1, element = Element.C),
                o2 to Atom(id = o2, element = Element.O),
            ),
            bonds = listOf(
                Bond(c0, c1, BondType.SINGLE),
                Bond(c1, o2, BondType.SINGLE),
            ),
        )

        assertTrue(molecule.rings.isEmpty())
    }

    @Test
    fun `分岐を持つが環を持たない分子のringsは空になる`() {
        val c0 = AtomId(0)
        val c1 = AtomId(1)
        val o2 = AtomId(2)
        val o3 = AtomId(3)
        val molecule = Molecule(
            atoms = mapOf(
                c0 to Atom(id = c0, element = Element.C),
                c1 to Atom(id = c1, element = Element.C),
                o2 to Atom(id = o2, element = Element.O),
                o3 to Atom(id = o3, element = Element.O),
            ),
            bonds = listOf(
                Bond(c0, c1, BondType.SINGLE),
                Bond(c1, o2, BondType.DOUBLE),
                Bond(c1, o3, BondType.SINGLE),
            ),
        )

        assertTrue(molecule.rings.isEmpty())
    }

    @Test
    fun `環閉包を含む分子は環を一周する原子の並びをringsとして返す`() {
        val c0 = AtomId(0)
        val c1 = AtomId(1)
        val c2 = AtomId(2)
        val molecule = Molecule(
            atoms = mapOf(
                c0 to Atom(id = c0, element = Element.C),
                c1 to Atom(id = c1, element = Element.C),
                c2 to Atom(id = c2, element = Element.C),
            ),
            bonds = listOf(
                Bond(c0, c1, BondType.SINGLE),
                Bond(c1, c2, BondType.SINGLE),
                Bond(c0, c2, BondType.SINGLE),
            ),
        )

        assertEquals(listOf(Ring(listOf(c0, c1, c2))), molecule.rings)
    }

    @Test
    fun `6員環は原子の並び順を保ったままringsとして返される`() {
        val c0 = AtomId(0)
        val c1 = AtomId(1)
        val c2 = AtomId(2)
        val c3 = AtomId(3)
        val c4 = AtomId(4)
        val c5 = AtomId(5)
        val molecule = Molecule(
            atoms = listOf(c0, c1, c2, c3, c4, c5).associateWith { Atom(id = it, element = Element.C) },
            bonds = listOf(
                Bond(c0, c1, BondType.AROMATIC),
                Bond(c1, c2, BondType.AROMATIC),
                Bond(c2, c3, BondType.AROMATIC),
                Bond(c3, c4, BondType.AROMATIC),
                Bond(c4, c5, BondType.AROMATIC),
                Bond(c0, c5, BondType.AROMATIC),
            ),
        )

        assertEquals(listOf(Ring(listOf(c0, c1, c2, c3, c4, c5))), molecule.rings)
    }
}

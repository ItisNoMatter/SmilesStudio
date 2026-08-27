package com.smilestudio.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MoleculeTest {

    @Test
    fun `molecule holds atoms and bonds by AtomId reference`() {
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
    fun `atom defaults reflect a neutral non-aromatic atom with unspecified hydrogen count`() {
        val atom = Atom(id = AtomId(0), element = Element.N)

        assertEquals(0, atom.charge)
        assertEquals(null, atom.isotope)
        assertEquals(false, atom.isAromatic)
        assertEquals(null, atom.hydrogenCount)
    }
}

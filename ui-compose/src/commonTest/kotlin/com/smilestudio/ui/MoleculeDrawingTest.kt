package com.smilestudio.ui

import com.smilestudio.core.Atom
import com.smilestudio.core.AtomId
import com.smilestudio.core.Bond
import com.smilestudio.core.BondType
import com.smilestudio.core.Element
import com.smilestudio.core.Molecule
import com.smilestudio.core.computeLayout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MoleculeDrawingTest {

    @Test
    fun `結合を持つ炭素原子にはラベルが描画されない`() {
        val c0 = AtomId(0)
        val c1 = AtomId(1)
        val molecule = Molecule(
            atoms = mapOf(c0 to Atom(c0, Element.C), c1 to Atom(c1, Element.C)),
            bonds = listOf(Bond(c0, c1, BondType.SINGLE)),
        )

        val commands = planMoleculeDrawing(molecule)

        assertTrue(commands.filterIsInstance<DrawCommand.AtomLabel>().isEmpty())
    }

    @Test
    fun `炭素以外の原子には元素記号のラベルが描画される`() {
        val c0 = AtomId(0)
        val o1 = AtomId(1)
        val molecule = Molecule(
            atoms = mapOf(c0 to Atom(c0, Element.C), o1 to Atom(o1, Element.O)),
            bonds = listOf(Bond(c0, o1, BondType.SINGLE)),
        )

        val commands = planMoleculeDrawing(molecule)
        val labels = commands.filterIsInstance<DrawCommand.AtomLabel>()

        assertEquals(1, labels.size)
        assertEquals("O", labels.single().text)
    }

    @Test
    fun `結合の数だけ線分の描画コマンドが生成される`() {
        val c0 = AtomId(0)
        val c1 = AtomId(1)
        val c2 = AtomId(2)
        val molecule = Molecule(
            atoms = mapOf(c0 to Atom(c0, Element.C), c1 to Atom(c1, Element.C), c2 to Atom(c2, Element.C)),
            bonds = listOf(Bond(c0, c1, BondType.SINGLE), Bond(c1, c2, BondType.SINGLE)),
        )

        val commands = planMoleculeDrawing(molecule)

        assertEquals(2, commands.filterIsInstance<DrawCommand.BondLine>().size)
    }

    @Test
    fun `芳香族結合はKekulize変換された単結合と二重結合の線分として描画される`() {
        val atomIds = (0..5).map { AtomId(it) }
        val atoms = atomIds.associateWith { Atom(it, Element.C) }
        val bonds = (0..4).map { i -> Bond(atomIds[i], atomIds[i + 1], BondType.AROMATIC) } +
            Bond(atomIds[5], atomIds[0], BondType.AROMATIC)
        val molecule = Molecule(atoms = atoms, bonds = bonds)

        val commands = planMoleculeDrawing(molecule)
        val lines = commands.filterIsInstance<DrawCommand.BondLine>()

        assertEquals(6, lines.size)
        assertTrue(lines.none { it.bondType == BondType.AROMATIC })
        assertEquals(3, lines.count { it.bondType == BondType.DOUBLE })
        assertEquals(3, lines.count { it.bondType == BondType.SINGLE })
    }

    @Test
    fun `結合を持たない孤立した炭素原子にはラベルが描画される`() {
        val c0 = AtomId(0)
        val molecule = Molecule(atoms = mapOf(c0 to Atom(c0, Element.C)), bonds = emptyList())

        val commands = planMoleculeDrawing(molecule)
        val labels = commands.filterIsInstance<DrawCommand.AtomLabel>()

        assertEquals(1, labels.size)
        assertEquals("C", labels.single().text)
    }

    @Test
    fun `線分の座標はレイアウト計算結果と一致する`() {
        val c0 = AtomId(0)
        val c1 = AtomId(1)
        val molecule = Molecule(
            atoms = mapOf(c0 to Atom(c0, Element.C), c1 to Atom(c1, Element.C)),
            bonds = listOf(Bond(c0, c1, BondType.SINGLE)),
        )
        val layout = computeLayout(molecule)

        val commands = planMoleculeDrawing(molecule)
        val line = commands.filterIsInstance<DrawCommand.BondLine>().single()

        assertTrue(
            (line.from == layout.getValue(c0) && line.to == layout.getValue(c1)) ||
                (line.from == layout.getValue(c1) && line.to == layout.getValue(c0)),
        )
    }
}

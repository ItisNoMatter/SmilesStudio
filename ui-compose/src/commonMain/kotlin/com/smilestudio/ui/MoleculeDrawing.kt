package com.smilestudio.ui

import com.smilestudio.core.BondType
import com.smilestudio.core.Element
import com.smilestudio.core.Molecule
import com.smilestudio.core.Point2D
import com.smilestudio.core.computeLayout
import com.smilestudio.core.kekulize

sealed interface DrawCommand {
    data class BondLine(val from: Point2D, val to: Point2D, val bondType: BondType) : DrawCommand
    data class AtomLabel(val position: Point2D, val text: String) : DrawCommand
}

fun planMoleculeDrawing(molecule: Molecule): List<DrawCommand> {
    val layout = computeLayout(molecule)

    val bondLines = kekulize(molecule).map { bond ->
        DrawCommand.BondLine(
            from = layout.getValue(bond.atom1),
            to = layout.getValue(bond.atom2),
            bondType = bond.type,
        )
    }

    val bondedAtomIds = molecule.bonds.flatMap { listOf(it.atom1, it.atom2) }.toSet()
    val atomLabels = molecule.atoms.values
        .filter { atom -> atom.element != Element.C || atom.id !in bondedAtomIds }
        .map { atom -> DrawCommand.AtomLabel(position = layout.getValue(atom.id), text = atom.element.symbol) }

    return bondLines + atomLabels
}

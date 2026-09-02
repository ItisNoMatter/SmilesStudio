package com.smilestudio.core

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private const val BOND_LENGTH = 1.0
private const val CHAIN_BOND_ANGLE_DEGREES = 120.0

/**
 * Computes 2D coordinates for every atom in [molecule], per AnyDR 0019/0045-0047: rings are laid
 * out as regular polygons, chains zigzag at 120-degree angles, and branch points fork
 * symmetrically at +/-120 degrees from the direction they were entered from.
 */
fun computeLayout(molecule: Molecule): Map<AtomId, Point2D> {
    if (molecule.atoms.isEmpty()) return emptyMap()

    val adjacency: Map<AtomId, List<Bond>> = buildMap<AtomId, MutableList<Bond>> {
        for (bond in molecule.bonds) {
            getOrPut(bond.atom1) { mutableListOf() }.add(bond)
            getOrPut(bond.atom2) { mutableListOf() }.add(bond)
        }
    }

    val ringByAtom: Map<AtomId, Ring> = buildMap {
        for (ring in molecule.rings) {
            for (atomId in ring.atoms) {
                putIfAbsent(atomId, ring)
            }
        }
    }

    val positions = mutableMapOf<AtomId, Point2D>()
    val visited = mutableSetOf<AtomId>()

    fun neighborOf(bond: Bond, atomId: AtomId): AtomId =
        if (bond.atom1 == atomId) bond.atom2 else bond.atom1

    fun unitVector(directionDegrees: Double): Point2D {
        val radians = directionDegrees * PI / 180.0
        return Point2D(cos(radians), sin(radians))
    }

    // placeOutgoing, placeRing, and placeFrom are mutually recursive; Kotlin local `fun`
    // declarations can't forward-reference each other, so they're declared as function-typed
    // vars up front and assigned below (each body may reference the others, since none of them
    // actually run until the final placeFrom/placeRing call at the bottom of this function).
    lateinit var placeOutgoing: (AtomId, Double, Int) -> Unit
    lateinit var placeRing: (Ring, AtomId, Point2D, Double, Int) -> Unit
    lateinit var placeFrom: (AtomId, Point2D, Double, Int) -> Unit

    placeOutgoing = { atomId, referenceDirection, initialTurnSign ->
        val outgoing = adjacency[atomId].orEmpty().filter { neighborOf(it, atomId) !in visited }
        when (outgoing.size) {
            0 -> Unit
            1 -> {
                val direction = referenceDirection + initialTurnSign * CHAIN_BOND_ANGLE_DEGREES
                placeFrom(neighborOf(outgoing[0], atomId), positions.getValue(atomId), direction, -initialTurnSign)
            }
            else -> {
                // Distributes outgoing bonds symmetrically at +/-120 degrees from the reference
                // direction. Exactly right for the common 2-way branch (a symmetric "Y" fork,
                // AnyDR 0047); a reasonable fallback for 3+ outgoing bonds, which is beyond v1's
                // "simple branch" grammar scope (AnyDR 0018) and has no dedicated test coverage.
                outgoing.forEachIndexed { index, bond ->
                    val sign = if (index % 2 == 0) 1 else -1
                    val direction = referenceDirection + sign * CHAIN_BOND_ANGLE_DEGREES
                    placeFrom(neighborOf(bond, atomId), positions.getValue(atomId), direction, -sign)
                }
            }
        }
    }

    placeRing = { ring, entryAtom, entryPosition, incomingDirection, initialTurnSign ->
        val n = ring.atoms.size
        val exteriorTurn = 360.0 / n
        val referenceDirection = incomingDirection + 180.0
        val firstEdgeDirection = referenceDirection + initialTurnSign * CHAIN_BOND_ANGLE_DEGREES

        val startIndex = ring.atoms.indexOf(entryAtom)
        val orderedRingAtoms = ring.atoms.subList(startIndex, n) + ring.atoms.subList(0, startIndex)

        var currentPosition = entryPosition
        var currentDirection = firstEdgeDirection
        positions[orderedRingAtoms[0]] = currentPosition
        visited += orderedRingAtoms[0]
        for (i in 1 until n) {
            currentPosition = currentPosition + unitVector(currentDirection) * BOND_LENGTH
            positions[orderedRingAtoms[i]] = currentPosition
            visited += orderedRingAtoms[i]
            currentDirection += exteriorTurn
        }

        val center = Point2D(
            orderedRingAtoms.sumOf { positions.getValue(it).x } / n,
            orderedRingAtoms.sumOf { positions.getValue(it).y } / n,
        )

        for (atom in orderedRingAtoms) {
            val outward = positions.getValue(atom) - center
            val outwardDirection = atan2(outward.y, outward.x) * 180.0 / PI
            val exocyclicBonds = adjacency[atom].orEmpty().filter { neighborOf(it, atom) !in visited }
            // Unlike placeOutgoing's +/-120 offset (which assumes a "straight ahead" continuation
            // direction that doesn't apply here), a single ring substituent points directly along
            // the outward radial direction -- that's already the correctly-bisected placement
            // relative to the ring's two fixed bond directions at this vertex (exactly so for a
            // regular hexagon, and a reasonable approximation for other ring sizes).
            if (exocyclicBonds.size == 1) {
                placeFrom(neighborOf(exocyclicBonds[0], atom), positions.getValue(atom), outwardDirection, 1)
            } else {
                // Rare/untested: 2+ substituents on one ring atom, beyond v1's target molecules.
                // Fan them symmetrically around the outward direction as a reasonable fallback.
                exocyclicBonds.forEachIndexed { index, bond ->
                    val sign = if (index % 2 == 0) 1 else -1
                    val direction = outwardDirection + sign * (CHAIN_BOND_ANGLE_DEGREES / 2)
                    placeFrom(neighborOf(bond, atom), positions.getValue(atom), direction, -sign)
                }
            }
        }
    }

    placeFrom = { atomId, parentPosition, incomingDirection, turnSign ->
        if (atomId in visited) {
            Unit
        } else {
            val position = parentPosition + unitVector(incomingDirection) * BOND_LENGTH
            val ring = ringByAtom[atomId]
            if (ring != null) {
                placeRing(ring, atomId, position, incomingDirection, turnSign)
            } else {
                positions[atomId] = position
                visited += atomId
                placeOutgoing(atomId, incomingDirection + 180.0, turnSign)
            }
        }
    }

    val startAtom = molecule.atoms.keys.minBy { it.value }
    val startRing = ringByAtom[startAtom]
    if (startRing != null) {
        placeRing(startRing, startAtom, Point2D(0.0, 0.0), 0.0, 1)
    } else {
        positions[startAtom] = Point2D(0.0, 0.0)
        visited += startAtom
        placeOutgoing(startAtom, 180.0, 1)
    }

    return positions
}

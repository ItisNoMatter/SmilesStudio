package com.smilestudio.core

import kotlin.math.acos
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val BOND_LENGTH = 1.0
private const val TOLERANCE = 1e-6

private fun distance(a: Point2D, b: Point2D): Double {
    val dx = a.x - b.x
    val dy = a.y - b.y
    return sqrt(dx * dx + dy * dy)
}

/** Angle between vectors `a->from` and `b->from`, in degrees, always in [0, 180]. */
private fun angleAtDegrees(from: Point2D, a: Point2D, b: Point2D): Double {
    val v1 = a - from
    val v2 = b - from
    val dot = v1.x * v2.x + v1.y * v2.y
    val cosAngle = (dot / (distance(from, a) * distance(from, b))).coerceIn(-1.0, 1.0)
    return acos(cosAngle) * 180.0 / kotlin.math.PI
}

private fun assertApprox(expected: Double, actual: Double, message: String) {
    assertTrue(kotlin.math.abs(expected - actual) < TOLERANCE, "$message (expected $expected, was $actual)")
}

class LayoutTest {

    @Test
    fun `原子を持たない分子のレイアウトは空になる`() {
        val molecule = Molecule(atoms = emptyMap(), bonds = emptyList())

        assertTrue(computeLayout(molecule).isEmpty())
    }

    @Test
    fun `結合を持たない単一原子は原点に配置される`() {
        val c0 = AtomId(0)
        val molecule = Molecule(atoms = mapOf(c0 to Atom(c0, Element.C)), bonds = emptyList())

        val layout = computeLayout(molecule)

        assertEquals(Point2D(0.0, 0.0), layout.getValue(c0))
    }

    @Test
    fun `2原子の結合は結合長1で配置される`() {
        val c0 = AtomId(0)
        val c1 = AtomId(1)
        val molecule = Molecule(
            atoms = mapOf(c0 to Atom(c0, Element.C), c1 to Atom(c1, Element.C)),
            bonds = listOf(Bond(c0, c1, BondType.SINGLE)),
        )

        val layout = computeLayout(molecule)

        assertApprox(BOND_LENGTH, distance(layout.getValue(c0), layout.getValue(c1)), "bond length")
    }

    @Test
    fun `3原子の鎖はジグザグになり結合角は120度になる`() {
        val c0 = AtomId(0)
        val c1 = AtomId(1)
        val c2 = AtomId(2)
        val molecule = Molecule(
            atoms = mapOf(c0 to Atom(c0, Element.C), c1 to Atom(c1, Element.C), c2 to Atom(c2, Element.C)),
            bonds = listOf(Bond(c0, c1, BondType.SINGLE), Bond(c1, c2, BondType.SINGLE)),
        )

        val layout = computeLayout(molecule)
        val p0 = layout.getValue(c0)
        val p1 = layout.getValue(c1)
        val p2 = layout.getValue(c2)

        assertApprox(BOND_LENGTH, distance(p0, p1), "bond c0-c1 length")
        assertApprox(BOND_LENGTH, distance(p1, p2), "bond c1-c2 length")
        assertApprox(120.0, angleAtDegrees(p1, p0, p2), "interior angle at c1")
    }

    @Test
    fun `分岐点は入ってきた結合との角度が120度になる対称なY字になる`() {
        val c0 = AtomId(0)
        val c1 = AtomId(1)
        val c2 = AtomId(2)
        val c3 = AtomId(3)
        val molecule = Molecule(
            atoms = mapOf(
                c0 to Atom(c0, Element.C),
                c1 to Atom(c1, Element.C),
                c2 to Atom(c2, Element.C),
                c3 to Atom(c3, Element.C),
            ),
            bonds = listOf(
                Bond(c0, c1, BondType.SINGLE),
                Bond(c1, c2, BondType.SINGLE),
                Bond(c1, c3, BondType.SINGLE),
            ),
        )

        val layout = computeLayout(molecule)
        val p0 = layout.getValue(c0)
        val p1 = layout.getValue(c1)
        val p2 = layout.getValue(c2)
        val p3 = layout.getValue(c3)

        assertApprox(BOND_LENGTH, distance(p1, p2), "bond c1-c2 length")
        assertApprox(BOND_LENGTH, distance(p1, p3), "bond c1-c3 length")
        assertApprox(120.0, angleAtDegrees(p1, p0, p2), "angle between incoming bond and first branch")
        assertApprox(120.0, angleAtDegrees(p1, p0, p3), "angle between incoming bond and second branch")
        assertApprox(120.0, angleAtDegrees(p1, p2, p3), "angle between the two branches")
    }

    @Test
    fun `6員環は正六角形として配置される`() {
        val atomIds = (0..5).map { AtomId(it) }
        val atoms = atomIds.associateWith { Atom(it, Element.C) }
        val bonds = (0..4).map { i -> Bond(atomIds[i], atomIds[i + 1], BondType.SINGLE) } +
            Bond(atomIds[5], atomIds[0], BondType.SINGLE)
        val molecule = Molecule(atoms = atoms, bonds = bonds)

        val layout = computeLayout(molecule)
        val positions = atomIds.map { layout.getValue(it) }

        // All 6 edges (including the ring-closing one) have length 1.
        for (i in 0..5) {
            val a = positions[i]
            val b = positions[(i + 1) % 6]
            assertApprox(BOND_LENGTH, distance(a, b), "ring edge $i-${(i + 1) % 6} length")
        }
        // Every interior angle of a regular hexagon is 120 degrees.
        for (i in 0..5) {
            val prev = positions[(i + 5) % 6]
            val current = positions[i]
            val next = positions[(i + 1) % 6]
            assertApprox(120.0, angleAtDegrees(current, prev, next), "interior angle at vertex $i")
        }
    }

    @Test
    fun `環に結合した置換基は環の中心から外向きに配置される`() {
        // A ring (0..5) with an extra atom (6) attached to ring atom 0 -- like toluene's
        // methyl carbon attached to the aromatic ring, but written ring-first for simplicity.
        val ringAtomIds = (0..5).map { AtomId(it) }
        val substituent = AtomId(6)
        val atoms = (ringAtomIds + substituent).associateWith { Atom(it, Element.C) }
        val ringBonds = (0..4).map { i -> Bond(ringAtomIds[i], ringAtomIds[i + 1], BondType.SINGLE) } +
            Bond(ringAtomIds[5], ringAtomIds[0], BondType.SINGLE)
        val bonds = ringBonds + Bond(ringAtomIds[0], substituent, BondType.SINGLE)
        val molecule = Molecule(atoms = atoms, bonds = bonds)

        val layout = computeLayout(molecule)
        val ringPositions = ringAtomIds.map { layout.getValue(it) }
        val center = Point2D(ringPositions.sumOf { it.x } / 6, ringPositions.sumOf { it.y } / 6)
        val attachmentPoint = layout.getValue(ringAtomIds[0])
        val substituentPosition = layout.getValue(substituent)

        assertApprox(BOND_LENGTH, distance(attachmentPoint, substituentPosition), "substituent bond length")
        // The substituent must be farther from the ring's center than its attachment point is --
        // i.e. it points outward, not back into the ring.
        assertTrue(
            distance(center, substituentPosition) > distance(center, attachmentPoint),
            "substituent should be farther from ring center than its attachment point",
        )
    }
}

package com.smilestudio.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SmilesParserTest {

    @Test
    fun `直鎖のSMILESをパースするとMoleculeを返す`() {
        val result = SmilesParser.parse("CCO")

        assertIs<ParseResult.Success>(result)
        val c0 = AtomId(0)
        val c1 = AtomId(1)
        val o2 = AtomId(2)
        assertEquals(
            Molecule(
                atoms = mapOf(
                    c0 to Atom(id = c0, element = Element.C),
                    c1 to Atom(id = c1, element = Element.C),
                    o2 to Atom(id = o2, element = Element.O),
                ),
                bonds = listOf(
                    Bond(c0, c1, BondType.SINGLE),
                    Bond(c1, o2, BondType.SINGLE),
                ),
            ),
            result.molecule,
        )
    }

    @Test
    fun `括弧による分岐を正しく解釈する`() {
        val result = SmilesParser.parse("CC(=O)O")

        assertIs<ParseResult.Success>(result)
        val c0 = AtomId(0)
        val c1 = AtomId(1)
        val o2 = AtomId(2)
        val o3 = AtomId(3)
        assertEquals(
            Molecule(
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
            ),
            result.molecule,
        )
    }

    @Test
    fun `結合記号を省略すると単結合として扱う`() {
        val result = SmilesParser.parse("CC")

        assertIs<ParseResult.Success>(result)
        assertEquals(BondType.SINGLE, result.molecule.bonds.single().type)
    }

    @Test
    fun `空文字列はエラーになる`() {
        val result = SmilesParser.parse("")

        assertIs<ParseResult.Failure>(result)
    }

    @Test
    fun `不明な文字はTokenizerのエラー理由をそのまま返す`() {
        val result = SmilesParser.parse("C$")

        assertIs<ParseResult.Failure>(result)
        assertEquals("位置1: 不明な文字 '$'", result.reason)
    }

    @Test
    fun `環閉包記法で環状分子をパースする`() {
        val result = SmilesParser.parse("C1CCC1")

        assertIs<ParseResult.Success>(result)
        val c0 = AtomId(0)
        val c1 = AtomId(1)
        val c2 = AtomId(2)
        val c3 = AtomId(3)
        assertEquals(
            Molecule(
                atoms = mapOf(
                    c0 to Atom(id = c0, element = Element.C),
                    c1 to Atom(id = c1, element = Element.C),
                    c2 to Atom(id = c2, element = Element.C),
                    c3 to Atom(id = c3, element = Element.C),
                ),
                bonds = listOf(
                    Bond(c0, c1, BondType.SINGLE),
                    Bond(c1, c2, BondType.SINGLE),
                    Bond(c2, c3, BondType.SINGLE),
                    Bond(c0, c3, BondType.SINGLE),
                ),
            ),
            result.molecule,
        )
    }

    @Test
    fun `環閉包ラベルは閉じた後に再利用できる`() {
        val result = SmilesParser.parse("C1CC1C1CC1")

        assertIs<ParseResult.Success>(result)
        val c0 = AtomId(0)
        val c1 = AtomId(1)
        val c2 = AtomId(2)
        val c3 = AtomId(3)
        val c4 = AtomId(4)
        val c5 = AtomId(5)
        assertEquals(
            Molecule(
                atoms = mapOf(
                    c0 to Atom(id = c0, element = Element.C),
                    c1 to Atom(id = c1, element = Element.C),
                    c2 to Atom(id = c2, element = Element.C),
                    c3 to Atom(id = c3, element = Element.C),
                    c4 to Atom(id = c4, element = Element.C),
                    c5 to Atom(id = c5, element = Element.C),
                ),
                bonds = listOf(
                    Bond(c0, c1, BondType.SINGLE),
                    Bond(c1, c2, BondType.SINGLE),
                    Bond(c0, c2, BondType.SINGLE),
                    Bond(c2, c3, BondType.SINGLE),
                    Bond(c3, c4, BondType.SINGLE),
                    Bond(c4, c5, BondType.SINGLE),
                    Bond(c3, c5, BondType.SINGLE),
                ),
            ),
            result.molecule,
        )
    }

    @Test
    fun `閉じられていない環閉包ラベルはエラーになる`() {
        val result = SmilesParser.parse("C1CC")

        assertIs<ParseResult.Failure>(result)
        assertEquals("位置1: 環閉包ラベル1が閉じられていません", result.reason)
    }

    @Test
    fun `環閉包ラベルが自身を参照しているとエラーになる`() {
        val result = SmilesParser.parse("C11")

        assertIs<ParseResult.Failure>(result)
        assertEquals("位置2: 環閉包ラベル1が自身を参照しています", result.reason)
    }

    @Test
    fun `原子の前に環閉包ラベルがあるとエラーになる`() {
        val result = SmilesParser.parse("1C")

        assertIs<ParseResult.Failure>(result)
        assertEquals("位置0: 環閉包ラベルの前に原子がありません", result.reason)
    }

    @Test
    fun `環閉包ラベルへの結合種別指定は未対応エラーになる`() {
        val result = SmilesParser.parse("C=1CCC1")

        assertIs<ParseResult.Failure>(result)
        assertEquals("位置1: 環閉包ラベルへの結合種別指定は未対応です", result.reason)
    }

    @Test
    fun `芳香族表記は未対応エラーになる`() {
        val result = SmilesParser.parse("c1ccccc1")

        assertIs<ParseResult.Failure>(result)
        assertEquals("位置0: 芳香族表記は未対応です", result.reason)
    }

    @Test
    fun `角括弧原子表記は未対応エラーになる`() {
        val result = SmilesParser.parse("[CH3]")

        assertIs<ParseResult.Failure>(result)
        assertEquals("位置0: 角括弧原子表記は未対応です", result.reason)
    }

    @Test
    fun `閉じ括弧が不足しているとエラーになる`() {
        val result = SmilesParser.parse("CC(=O")

        assertIs<ParseResult.Failure>(result)
        assertEquals("位置2: 閉じ括弧がありません", result.reason)
    }

    @Test
    fun `対応しない閉じ括弧はエラーになる`() {
        val result = SmilesParser.parse("CC)")

        assertIs<ParseResult.Failure>(result)
        assertEquals("位置2: 対応する開き括弧がありません", result.reason)
    }

    @Test
    fun `結合記号の後に原子がないとエラーになる`() {
        val result = SmilesParser.parse("C=")

        assertIs<ParseResult.Failure>(result)
        assertEquals("位置1: 結合記号の後に原子がありません", result.reason)
    }

    @Test
    fun `先頭の結合記号はエラーになる`() {
        val result = SmilesParser.parse("=C")

        assertIs<ParseResult.Failure>(result)
        assertEquals("位置0: 結合記号の前に原子がありません", result.reason)
    }
}

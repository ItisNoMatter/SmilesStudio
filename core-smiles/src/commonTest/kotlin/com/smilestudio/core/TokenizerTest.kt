package com.smilestudio.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TokenizerTest {

    @Test
    fun `1文字の元素記号をAtomSymbolトークンに変換する`() {
        val result = Tokenizer.tokenize("C")

        assertIs<TokenizeResult.Success>(result)
        assertEquals(
            listOf(PositionedToken(Token.AtomSymbol(Element.C), position = 0)),
            result.tokens,
        )
    }

    @Test
    fun `2文字の元素記号Clをひとつのトークンに変換する`() {
        val result = Tokenizer.tokenize("Cl")

        assertIs<TokenizeResult.Success>(result)
        assertEquals(
            listOf(PositionedToken(Token.AtomSymbol(Element.CL), position = 0)),
            result.tokens,
        )
    }

    @Test
    fun `2文字の元素記号Brをひとつのトークンに変換する`() {
        val result = Tokenizer.tokenize("Br")

        assertIs<TokenizeResult.Success>(result)
        assertEquals(
            listOf(PositionedToken(Token.AtomSymbol(Element.BR), position = 0)),
            result.tokens,
        )
    }

    @Test
    fun `結合記号をBondSymbolトークンに変換する`() {
        val double = Tokenizer.tokenize("=")
        val triple = Tokenizer.tokenize("#")
        val single = Tokenizer.tokenize("-")

        assertIs<TokenizeResult.Success>(double)
        assertIs<TokenizeResult.Success>(triple)
        assertIs<TokenizeResult.Success>(single)
        assertEquals(listOf(PositionedToken(Token.BondSymbol(BondType.DOUBLE), 0)), double.tokens)
        assertEquals(listOf(PositionedToken(Token.BondSymbol(BondType.TRIPLE), 0)), triple.tokens)
        assertEquals(listOf(PositionedToken(Token.BondSymbol(BondType.SINGLE), 0)), single.tokens)
    }

    @Test
    fun `括弧をLParenとRParenトークンに変換する`() {
        val result = Tokenizer.tokenize("()")

        assertIs<TokenizeResult.Success>(result)
        assertEquals(
            listOf(
                PositionedToken(Token.LParen, 0),
                PositionedToken(Token.RParen, 1),
            ),
            result.tokens,
        )
    }

    @Test
    fun `直鎖のSMILES文字列を複数のトークンに変換する`() {
        val result = Tokenizer.tokenize("CCO")

        assertIs<TokenizeResult.Success>(result)
        assertEquals(
            listOf(
                PositionedToken(Token.AtomSymbol(Element.C), 0),
                PositionedToken(Token.AtomSymbol(Element.C), 1),
                PositionedToken(Token.AtomSymbol(Element.O), 2),
            ),
            result.tokens,
        )
    }

    @Test
    fun `分岐を含むSMILES文字列をトークンに変換する`() {
        val result = Tokenizer.tokenize("CC(=O)O")

        assertIs<TokenizeResult.Success>(result)
        assertEquals(
            listOf(
                PositionedToken(Token.AtomSymbol(Element.C), 0),
                PositionedToken(Token.AtomSymbol(Element.C), 1),
                PositionedToken(Token.LParen, 2),
                PositionedToken(Token.BondSymbol(BondType.DOUBLE), 3),
                PositionedToken(Token.AtomSymbol(Element.O), 4),
                PositionedToken(Token.RParen, 5),
                PositionedToken(Token.AtomSymbol(Element.O), 6),
            ),
            result.tokens,
        )
    }

    @Test
    fun `不明な文字は位置情報付きのエラーを返す`() {
        val result = Tokenizer.tokenize("C$")

        assertIs<TokenizeResult.Failure>(result)
        assertEquals("位置1: 不明な文字 '$'", result.reason)
    }

    @Test
    fun `環閉包ラベルをRingClosureトークンに変換する`() {
        val result = Tokenizer.tokenize("C1")

        assertIs<TokenizeResult.Success>(result)
        assertEquals(
            listOf(
                PositionedToken(Token.AtomSymbol(Element.C), 0),
                PositionedToken(Token.RingClosure(1), 1),
            ),
            result.tokens,
        )
    }

    @Test
    fun `連続する環閉包ラベルはそれぞれ独立したトークンに変換する`() {
        val result = Tokenizer.tokenize("C12")

        assertIs<TokenizeResult.Success>(result)
        assertEquals(
            listOf(
                PositionedToken(Token.AtomSymbol(Element.C), 0),
                PositionedToken(Token.RingClosure(1), 1),
                PositionedToken(Token.RingClosure(2), 2),
            ),
            result.tokens,
        )
    }

    @Test
    fun `芳香族の小文字表記は専用の未対応理由を返す`() {
        val result = Tokenizer.tokenize("c")

        assertIs<TokenizeResult.Failure>(result)
        assertEquals("位置0: 芳香族表記は未対応です", result.reason)
    }

    @Test
    fun `角括弧原子表記は専用の未対応理由を返す`() {
        val result = Tokenizer.tokenize("[CH3]")

        assertIs<TokenizeResult.Failure>(result)
        assertEquals("位置0: 角括弧原子表記は未対応です", result.reason)
    }
}

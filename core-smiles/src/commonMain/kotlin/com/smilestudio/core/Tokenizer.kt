package com.smilestudio.core

object Tokenizer {

    private val twoCharElements = mapOf(
        "Cl" to Element.CL,
        "Br" to Element.BR,
    )

    private val oneCharElements = mapOf(
        "C" to Element.C,
        "N" to Element.N,
        "O" to Element.O,
        "F" to Element.F,
        "P" to Element.P,
        "S" to Element.S,
        "H" to Element.H,
        "I" to Element.I,
    )

    private val aromaticSubset = setOf('b', 'c', 'n', 'o', 'p', 's')

    fun tokenize(smiles: String): TokenizeResult {
        val tokens = mutableListOf<PositionedToken>()
        var i = 0
        while (i < smiles.length) {
            val c = smiles[i]
            when {
                c == '(' -> {
                    tokens += PositionedToken(Token.LParen, i)
                    i += 1
                }
                c == ')' -> {
                    tokens += PositionedToken(Token.RParen, i)
                    i += 1
                }
                c == '=' -> {
                    tokens += PositionedToken(Token.BondSymbol(BondType.DOUBLE), i)
                    i += 1
                }
                c == '#' -> {
                    tokens += PositionedToken(Token.BondSymbol(BondType.TRIPLE), i)
                    i += 1
                }
                c == '-' -> {
                    tokens += PositionedToken(Token.BondSymbol(BondType.SINGLE), i)
                    i += 1
                }
                c == '[' -> return TokenizeResult.Failure("位置$i: 角括弧原子表記は未対応です")
                c.isDigit() -> {
                    tokens += PositionedToken(Token.RingClosure(c.digitToInt()), i)
                    i += 1
                }
                c in aromaticSubset -> return TokenizeResult.Failure("位置$i: 芳香族表記は未対応です")
                c.isUpperCase() -> {
                    val twoChar = smiles.substring(i, minOf(i + 2, smiles.length))
                    val twoCharElement = twoCharElements[twoChar]
                    if (twoCharElement != null) {
                        tokens += PositionedToken(Token.AtomSymbol(twoCharElement), i)
                        i += 2
                    } else {
                        val oneCharElement = oneCharElements[c.toString()]
                            ?: return TokenizeResult.Failure("位置$i: 不明な文字 '$c'")
                        tokens += PositionedToken(Token.AtomSymbol(oneCharElement), i)
                        i += 1
                    }
                }
                else -> return TokenizeResult.Failure("位置$i: 不明な文字 '$c'")
            }
        }
        return TokenizeResult.Success(tokens)
    }
}

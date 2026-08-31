package com.smilestudio.core

object SmilesParser {
    fun parse(smiles: String): ParseResult {
        val tokenizeResult = Tokenizer.tokenize(smiles)
        val tokens = when (tokenizeResult) {
            is TokenizeResult.Failure -> return ParseResult.Failure(tokenizeResult.reason)
            is TokenizeResult.Success -> tokenizeResult.tokens
        }
        if (tokens.isEmpty()) {
            return ParseResult.Failure("空のSMILES文字列です")
        }
        return parseTokens(tokens)
    }

    private fun parseTokens(tokens: List<PositionedToken>): ParseResult {
        val atoms = mutableMapOf<AtomId, Atom>()
        val bonds = mutableListOf<Bond>()
        val branchStack = ArrayDeque<Pair<AtomId?, Int>>()
        var nextAtomId = 0
        var currentAtom: AtomId? = null
        var pendingBond: BondType? = null
        var pendingBondPosition: Int? = null

        for (positioned in tokens) {
            when (val token = positioned.token) {
                is Token.AtomSymbol -> {
                    val parent = currentAtom
                    if (pendingBond != null && parent == null) {
                        return ParseResult.Failure("位置$pendingBondPosition: 結合記号の前に原子がありません")
                    }
                    val newAtomId = AtomId(nextAtomId++)
                    atoms[newAtomId] = Atom(id = newAtomId, element = token.element)
                    if (parent != null) {
                        bonds += Bond(atom1 = parent, atom2 = newAtomId, type = pendingBond ?: BondType.SINGLE)
                    }
                    pendingBond = null
                    pendingBondPosition = null
                    currentAtom = newAtomId
                }
                is Token.BondSymbol -> {
                    if (pendingBond != null) {
                        return ParseResult.Failure("位置${positioned.position}: 結合記号が連続しています")
                    }
                    pendingBond = token.bondType
                    pendingBondPosition = positioned.position
                }
                Token.LParen -> {
                    if (currentAtom == null) {
                        return ParseResult.Failure("位置${positioned.position}: 分岐の開始位置に原子がありません")
                    }
                    branchStack.addLast(currentAtom to positioned.position)
                }
                Token.RParen -> {
                    if (pendingBond != null) {
                        return ParseResult.Failure("位置$pendingBondPosition: 結合記号の後に原子がありません")
                    }
                    if (branchStack.isEmpty()) {
                        return ParseResult.Failure("位置${positioned.position}: 対応する開き括弧がありません")
                    }
                    currentAtom = branchStack.removeLast().first
                }
            }
        }

        if (pendingBond != null) {
            return ParseResult.Failure("位置$pendingBondPosition: 結合記号の後に原子がありません")
        }
        if (branchStack.isNotEmpty()) {
            return ParseResult.Failure("位置${branchStack.last().second}: 閉じ括弧がありません")
        }

        return ParseResult.Success(Molecule(atoms = atoms, bonds = bonds))
    }
}

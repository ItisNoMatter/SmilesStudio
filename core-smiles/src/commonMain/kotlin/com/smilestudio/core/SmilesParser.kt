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
        val pendingRingClosures = mutableMapOf<Int, Pair<AtomId, Int>>()
        val aromaticNotationAtoms = mutableSetOf<AtomId>()
        var nextAtomId = 0
        var currentAtom: AtomId? = null
        var pendingBond: BondType? = null
        var pendingBondPosition: Int? = null

        fun addAtom(element: Element, aromatic: Boolean): ParseResult.Failure? {
            val parent = currentAtom
            if (pendingBond != null && parent == null) {
                return ParseResult.Failure("位置$pendingBondPosition: 結合記号の前に原子がありません")
            }
            val newAtomId = AtomId(nextAtomId++)
            atoms[newAtomId] = Atom(id = newAtomId, element = element)
            if (parent != null) {
                val bondType = pendingBond
                    ?: if (aromatic && parent in aromaticNotationAtoms) BondType.AROMATIC else BondType.SINGLE
                bonds += Bond(atom1 = parent, atom2 = newAtomId, type = bondType)
            }
            if (aromatic) {
                aromaticNotationAtoms += newAtomId
            }
            pendingBond = null
            pendingBondPosition = null
            currentAtom = newAtomId
            return null
        }

        for (positioned in tokens) {
            when (val token = positioned.token) {
                is Token.AtomSymbol -> {
                    addAtom(token.element, aromatic = false)?.let { return it }
                }
                is Token.AromaticAtomSymbol -> {
                    addAtom(token.element, aromatic = true)?.let { return it }
                }
                is Token.BondSymbol -> {
                    if (pendingBond != null) {
                        return ParseResult.Failure("位置${positioned.position}: 結合記号が連続しています")
                    }
                    pendingBond = token.bondType
                    pendingBondPosition = positioned.position
                }
                is Token.RingClosure -> {
                    val atom = currentAtom
                        ?: return ParseResult.Failure("位置${positioned.position}: 環閉包ラベルの前に原子がありません")
                    if (pendingBond != null) {
                        return ParseResult.Failure("位置$pendingBondPosition: 環閉包ラベルへの結合種別指定は未対応です")
                    }
                    val pending = pendingRingClosures.remove(token.label)
                    if (pending == null) {
                        pendingRingClosures[token.label] = atom to positioned.position
                    } else {
                        val (partnerAtom, _) = pending
                        if (partnerAtom == atom) {
                            return ParseResult.Failure("位置${positioned.position}: 環閉包ラベル${token.label}が自身を参照しています")
                        }
                        val bondType = if (atom in aromaticNotationAtoms && partnerAtom in aromaticNotationAtoms) {
                            BondType.AROMATIC
                        } else {
                            BondType.SINGLE
                        }
                        bonds += Bond(atom1 = partnerAtom, atom2 = atom, type = bondType)
                    }
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
        if (pendingRingClosures.isNotEmpty()) {
            val (label, pending) = pendingRingClosures.entries.first()
            return ParseResult.Failure("位置${pending.second}: 環閉包ラベル${label}が閉じられていません")
        }

        return ParseResult.Success(Molecule(atoms = atoms, bonds = bonds))
    }
}

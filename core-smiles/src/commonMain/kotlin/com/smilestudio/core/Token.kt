package com.smilestudio.core

sealed interface Token {
    data class AtomSymbol(val element: Element) : Token
    data class BondSymbol(val bondType: BondType) : Token
    data class RingClosure(val label: Int) : Token
    data object LParen : Token
    data object RParen : Token
}

package com.smilestudio.core

sealed interface Token {
    data class AtomSymbol(val element: Element) : Token
    data class BondSymbol(val bondType: BondType) : Token
    data object LParen : Token
    data object RParen : Token
}

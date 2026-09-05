package com.smilestudio.ui

import com.smilestudio.core.ParseResult
import com.smilestudio.core.SmilesParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MoleculeEditorStateTest {

    @Test
    fun `有効なSMILESはMoleculeとして解決されエラーはnull`() {
        val state = resolveMoleculeEditorState(smilesText = "CCO", previousMolecule = null)

        val expected = (SmilesParser.parse("CCO") as ParseResult.Success).molecule
        assertEquals(expected, state.molecule)
        assertNull(state.errorMessage)
    }

    @Test
    fun `無効なSMILESは直前のMoleculeを保持しつつエラー文言を返す`() {
        val previous = (SmilesParser.parse("CCO") as ParseResult.Success).molecule

        val state = resolveMoleculeEditorState(smilesText = "((", previousMolecule = previous)

        assertEquals(previous, state.molecule)
        assertEquals((SmilesParser.parse("((") as ParseResult.Failure).reason, state.errorMessage)
    }

    @Test
    fun `空文字は直前のMoleculeをそのまま保持しエラーも出さない`() {
        val previous = (SmilesParser.parse("CCO") as ParseResult.Success).molecule

        val state = resolveMoleculeEditorState(smilesText = "", previousMolecule = previous)

        assertEquals(previous, state.molecule)
        assertNull(state.errorMessage)
    }
}

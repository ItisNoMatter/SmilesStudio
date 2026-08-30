package com.smilestudio.core

import kotlin.test.Test
import kotlin.test.assertIs

class SmilesParserTest {

    @Test
    fun `パース処理が未実装の間はFailureを返す`() {
        val result = SmilesParser.parse("C")

        assertIs<ParseResult.Failure>(result)
    }
}

/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.markdown.parse

import kotlin.test.Test
import kotlin.test.assertEquals

class AstTest {

    @Test
    fun header1() {
        val source = "# Header"
        val expected = listOf(
            MarkdownHeaderAstEntry(1, listOf(MarkdownInlineAstEntry("Header", bold = false, italic = false)))
        )
        val actual = ast(tokenize(source))
        assertEquals(expected, actual)
    }

    @Test
    fun header4() {
        val source = "#### Header"
        val expected = listOf(
            MarkdownHeaderAstEntry(4, listOf(MarkdownInlineAstEntry("Header", bold = false, italic = false)))
        )
        val actual = ast(tokenize(source))
        assertEquals(expected, actual)
    }

    @Test
    fun bulletListSame() {
        val source = """
            * item 1
            * item 2
        """.trimIndent()

        val expected = listOf(
            MarkdownListAstEntry(true, 1, listOf(MarkdownInlineAstEntry("item 1", bold = false, italic = false))),
            MarkdownListAstEntry(true, 1, listOf(MarkdownInlineAstEntry("item 2", bold = false, italic = false))),
        )

        val actual = ast(tokenize(source))
        assertEquals(expected, actual)
    }

    @Test
    fun bulletListOne() {
        val source = """
            * item 1
              * item 1.1
            * item 2
        """.trimIndent()

        val expected = listOf(
            MarkdownListAstEntry(true, 1, listOf(MarkdownInlineAstEntry("item 1", bold = false, italic = false))),
            MarkdownListAstEntry(true, 2, listOf(MarkdownInlineAstEntry("item 1.1", bold = false, italic = false))),
            MarkdownListAstEntry(true, 1, listOf(MarkdownInlineAstEntry("item 2", bold = false, italic = false))),
        )

        val actual = ast(tokenize(source))
        assertEquals(expected, actual)
    }

    @Test
    fun bulletList() {
        val source = """
            * item 1
              * item 1.1
                  * item 1.1.1
              * item 1.2
            * item 2
        """.trimIndent()

        val expected = listOf(
            MarkdownListAstEntry(true, 1, listOf(MarkdownInlineAstEntry("item 1", bold = false, italic = false))),
            MarkdownListAstEntry(true, 2, listOf(MarkdownInlineAstEntry("item 1.1", bold = false, italic = false))),
            MarkdownListAstEntry(true, 3, listOf(MarkdownInlineAstEntry("item 1.1.1", bold = false, italic = false))),
            MarkdownListAstEntry(true, 2, listOf(MarkdownInlineAstEntry("item 1.2", bold = false, italic = false))),
            MarkdownListAstEntry(true, 1, listOf(MarkdownInlineAstEntry("item 2", bold = false, italic = false))),
        )

        val actual = ast(tokenize(source))
        assertEquals(expected, actual)
    }

    @Test
    fun numberList() {
        val source = """
            1. item 1
              1. item 1.1
                  1. item 1.1.1
              1. item 1.2
            1. item 2
        """.trimIndent()

        val expected = listOf(
            MarkdownListAstEntry(false, 1, listOf(MarkdownInlineAstEntry("item 1", bold = false, italic = false))),
            MarkdownListAstEntry(false, 2, listOf(MarkdownInlineAstEntry("item 1.1", bold = false, italic = false))),
            MarkdownListAstEntry(false, 3, listOf(MarkdownInlineAstEntry("item 1.1.1", bold = false, italic = false))),
            MarkdownListAstEntry(false, 2, listOf(MarkdownInlineAstEntry("item 1.2", bold = false, italic = false))),
            MarkdownListAstEntry(false, 1, listOf(MarkdownInlineAstEntry("item 2", bold = false, italic = false))),
        )

        val actual = ast(tokenize(source))
        assertEquals(expected, actual)
    }

    @Test
    fun mixedList() {
        val source = """
            1. item 1
              * item 2
            1. item 3
        """.trimIndent()

        val expected = listOf(
            MarkdownListAstEntry(false, 1, listOf(MarkdownInlineAstEntry("item 1", bold = false, italic = false))),
            MarkdownListAstEntry(true, 1, listOf(MarkdownInlineAstEntry("item 2", bold = false, italic = false))),
            MarkdownListAstEntry(false, 1, listOf(MarkdownInlineAstEntry("item 3", bold = false, italic = false))),
        )

        val actual = ast(tokenize(source))
        assertEquals(expected, actual)
    }


    @Test
    fun codeFence() {
        val source = "```\ncode\n```"
        val expected = listOf(
            MarkdownCodeFenceAstEntry(null, "code")
        )
        val actual = ast(tokenize(source))
        assertEquals(expected, actual)
    }

    @Test
    fun codeFenceWithLanguage() {
        val source = "```text\ncode\n```"
        val expected = listOf(
            MarkdownCodeFenceAstEntry("text", "code")
        )
        val actual = ast(tokenize(source))
        assertEquals(expected, actual)
    }

    @Test
    fun boldText() {
        val source = "**bold text**"
        val expected = listOf(
            MarkdownParagraphAstEntry(
                mutableListOf(MarkdownInlineAstEntry("bold text", true, italic = false)),
                false
            )
        )
        val actual = ast(tokenize(source))
        assertEquals(expected, actual)
    }

    @Test
    fun quote() {
        val source = ">This is a quote\n>and another quote"
        val expected = listOf(
            MarkdownQuoteEntry(
                listOf(
                    MarkdownParagraphAstEntry(
                        mutableListOf(
                            MarkdownInlineAstEntry("This is a quote", false, italic = false),
                            MarkdownInlineAstEntry(" ", false, italic = false),
                            MarkdownInlineAstEntry("and another quote", false, italic = false)
                        ),
                        false
                    )
                )
            )
        )

        val actual = ast(tokenize(source))

        assertEquals(expected, actual)
    }

    @Test
    fun horizontalRule() {
        val source = "---"
        val expected = listOf(
            MarkdownHorizontalRuleAstEntry()
        )
        val actual = ast(tokenize(source))
        assertEquals(expected, actual)
    }

    @Test
    fun codeSpan() {
        val source = "`code`"
        val expected = listOf(
            MarkdownParagraphAstEntry(
                mutableListOf(MarkdownInlineAstEntry("code", bold = false, italic = false, code = true)),
                false
            )
        )
        val actual = ast(tokenize(source))
        assertEquals(expected, actual)
    }

    @Test
    fun crazyCodeSpan() {
        val source = "`` ` ``"
        val expected = listOf(
            MarkdownParagraphAstEntry(
                mutableListOf(MarkdownInlineAstEntry("`", bold = false, italic = false, code = true)),
                false
            )
        )
        val actual = ast(tokenize(source))
        assertEquals(expected, actual)
    }

    @Test
    fun multipleInlineFormats() {
        val source = "This **text** is *partially* `formatted`"
        val expected = listOf(
            MarkdownParagraphAstEntry(
                mutableListOf(
                    MarkdownInlineAstEntry("This ", bold = false, italic = false),
                    MarkdownInlineAstEntry("text", bold = true, italic = false),
                    MarkdownInlineAstEntry(" is ", bold = false, italic = false),
                    MarkdownInlineAstEntry("partially", bold = false, italic = true),
                    MarkdownInlineAstEntry(" ", bold = false, italic = false),
                    MarkdownInlineAstEntry("formatted", bold = false, italic = false, code = true)
                ),
                false
            )
        )
        val actual = ast(tokenize(source))
        assertEquals(expected, actual)
    }
}
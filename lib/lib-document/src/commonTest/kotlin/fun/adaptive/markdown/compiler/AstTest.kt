/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.markdown.compiler

import `fun`.adaptive.markdown.model.*
import `fun`.adaptive.markdown.transform.MarkdownAstDumpVisitor.Companion.dump
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * IMPORTANT remember [dump]
 */
class AstTest {

    fun ast(source: String) =
        MarkdownCompiler.ast(source)

    infix fun String.assertEquals(expected: ASTBuilder.() -> Unit) =
        assertEquals(ASTBuilder().apply { expected() }.entries.dump(), ast(this).dump())

    @Test
    fun header1() {
        val source = "# Header"
        val expected = listOf(
            MarkdownHeader(1, mutableListOf(MarkdownInline("Header", bold = false, italic = false)))
        )
        val actual = ast(source)
        assertEquals(expected.dump(), actual.dump())
    }

    @Test
    fun header4() {
        val source = "#### Header"
        val expected = listOf(
            MarkdownHeader(4, mutableListOf(MarkdownInline("Header", bold = false, italic = false)))
        )
        val actual = ast(source)
        assertEquals(expected.dump(), actual.dump())
    }

    @Test
    fun bulletListSame() {
        val source = """
            * item 1
            * item 2
        """.trimIndent()

        source assertEquals {
            + list {
                + item(label = "item 1")
                + item(label = "item 2")
            }
        }
    }

    @Test
    fun bulletListOne() {
        val source = """
            * item 1
              * item 1.1
            * item 2
        """.trimIndent()

        source assertEquals {
            + list {
                + item(label = "item 1") {
                    + item(2, label = "item 1.1")
                }
                + item(label = "item 2")
            }
        }
    }

    @Test
    fun bulletListTwo() {
        val source = """
            * item 1
              * item 1.1
              * item 1.2
            * item 2
        """.trimIndent()

        source assertEquals {
            + list {
                + item(label = "item 1") {
                    + item(2, label = "item 1.1")
                    + item(2, label = "item 1.2")
                }
                + item(label = "item 2")
            }
        }
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

        source assertEquals {
            + list {
                + item(label = "item 1") {
                    + item(2, label = "item 1.1") {
                        + item(3, label = "item 1.1.1")
                    }
                    + item(2, label = "item 1.2")
                }
                + item(label = "item 2")
            }
        }
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

        source assertEquals {
            + list(bullet = false) {
                + item(bullet = false, label = "item 1") {
                    + item(2, bullet = false, label = "item 1.1") {
                        + item(3, bullet = false, label = "item 1.1.1")
                    }
                    + item(2, bullet = false, label = "item 1.2")
                }
                + item(bullet = false, label = "item 2")
            }
        }
    }

    @Test
    fun mixedList() {
        val source = """
            1. item 1
              * item 2
            1. item 3
        """.trimIndent()

        source assertEquals {
            + list(bullet = false) {
                + item(bullet = false, label = "item 1", subBullet = true) {
                    + item(2, label = "item 2")
                }
                + item(bullet = false, label = "item 3")
            }
        }
    }

    @Test
    fun consequentLists() {
        val source = """
            * List item 1

            1. List item 1
        """.trimIndent()

        source assertEquals {
            + list {
                + item(label = "List item 1")
            }
            + list(bullet = false) {
                + item(bullet = false, label = "List item 1")
            }
        }
    }

    @Test
    fun codeFence() {
        val source = "```\ncode\n```"
        val expected = listOf(
            MarkdownCodeFence(null, "code")
        )
        val actual = ast(source)
        assertEquals(expected.dump(), actual.dump())
    }

    @Test
    fun codeFenceWithLanguage() {
        val source = "```text\ncode\n```"
        val expected = listOf(
            MarkdownCodeFence("text", "code")
        )
        val actual = ast(source)
        assertEquals(expected.dump(), actual.dump())
    }

    @Test
    fun boldTextAsterisk() {
        val source = "**bold text**"
        val expected = listOf(
            MarkdownParagraph(
                mutableListOf(MarkdownInline("bold text", true, italic = false)),
                false
            )
        )
        val actual = ast(source)
        assertEquals(expected.dump(), actual.dump())
    }

    @Test
    fun boldTextAsteriskHyphen() {
        val source = "**bold-text**"
        val expected = listOf(
            MarkdownParagraph(
                mutableListOf(MarkdownInline("bold-text", true, italic = false)),
                false
            )
        )
        val actual = ast(source)
        assertEquals(expected.dump(), actual.dump())
    }

    @Test
    fun boldTextUnderscore() {
        val source = "__bold text__"
        val expected = listOf(
            MarkdownParagraph(
                mutableListOf(MarkdownInline("bold text", true, italic = false)),
                false
            )
        )
        val actual = ast(source)
        assertEquals(expected.dump(), actual.dump())
    }

    @Test
    fun italicTextAsterisk() {
        val source = "*italic text*"
        val expected = listOf(
            MarkdownParagraph(
                mutableListOf(MarkdownInline("italic text", false, italic = true)),
                false
            )
        )
        val actual = ast(source)
        assertEquals(expected.dump(), actual.dump())
    }

    @Test
    fun italicTextUnderscore() {
        val source = "_italic text_"
        val expected = listOf(
            MarkdownParagraph(
                mutableListOf(MarkdownInline("italic text", false, italic = true)),
                false
            )
        )
        val actual = ast(source)
        assertEquals(expected.dump(), actual.dump())
    }

    @Test
    fun boldAndItalicText() {
        val source = "**_bold italic_**"
        val expected = listOf(
            MarkdownParagraph(
                mutableListOf(MarkdownInline("bold italic", true, italic = true)),
                false
            )
        )
        val actual = ast(source)
        assertEquals(expected.dump(), actual.dump())
    }

    @Test
    fun quote() {
        val source = ">This is a quote\n>and another quote"
        val expected = listOf(
            MarkdownQuote(
                mutableListOf(
                    MarkdownParagraph(
                        mutableListOf(
                            MarkdownInline("This is a quote", false, italic = false),
                            MarkdownInline(" and another quote", false, italic = false)
                        ),
                        false
                    )
                )
            )
        )

        val actual = ast(source)

        assertEquals(expected.dump(), actual.dump())
    }

    @Test
    fun horizontalRule() {
        val source = "---"
        val expected = listOf(
            MarkdownHorizontalRule()
        )
        val actual = ast(source)
        assertEquals(expected.dump(), actual.dump())
    }

    @Test
    fun codeSpan() {
        val source = "`code`"
        val expected = listOf(
            MarkdownParagraph(
                mutableListOf(MarkdownInline("code", bold = false, italic = false, code = true)),
                false
            )
        )
        val actual = ast(source)
        assertEquals(expected.dump(), actual.dump())
    }

    @Test
    fun crazyCodeSpan() {
        val source = "`` ` ``"
        val expected = listOf(
            MarkdownParagraph(
                mutableListOf(MarkdownInline("`", bold = false, italic = false, code = true)),
                false
            )
        )
        val actual = ast(source)
        assertEquals(expected.dump(), actual.dump())
    }

    @Test
    fun multipleInlineFormats() {
        val source = "This **text** is *partially* `formatted`"
        val expected = listOf(
            MarkdownParagraph(
                mutableListOf(
                    MarkdownInline("This ", bold = false, italic = false),
                    MarkdownInline("text", bold = true, italic = false),
                    MarkdownInline(" is ", bold = false, italic = false),
                    MarkdownInline("partially", bold = false, italic = true),
                    MarkdownInline(" ", bold = false, italic = false),
                    MarkdownInline("formatted", bold = false, italic = false, code = true)
                ),
                false
            )
        )
        val actual = ast(source)
        assertEquals(expected.dump(), actual.dump())
    }

    @Test
    fun doubleQuote() {
        val source = "> > This is a double quote\n>and another quote"

        source assertEquals {
            + quote {
                + quote {
                    + paragraph { "This is a double quote" }
                }
                + paragraph { "and another quote" }
            }
        }
    }

    @Test
    fun listInQuote() {
        val source = "> * item 1\n> * item 2"

        source assertEquals {
            + quote {
                + list {
                    + item(label = "item 1")
                    + item(label = "item 2")
                }
            }
        }
    }

    @Test
    fun quoteSeparation() {
        val source = "> quote 1\n\n> quote 2"

        source assertEquals {
            + quote {
                + paragraph { "quote 1" }
            }
            + quote {
                + paragraph { "quote 2" }
            }
        }
    }

    @Test
    fun multiParagraphQuote() {
        val source = "> quote 1\n>\n> quote 2"

        source assertEquals {
            + quote {
                + paragraph(closed = true) { "quote 1" }
                + paragraph { "quote 2" }
            }
        }
    }

    @Test
    fun link() {
        val source = "[IntelliJ IDEA](https://www.jetbrains.com/idea/)"

        source assertEquals {
            + link { "[IntelliJ IDEA](https://www.jetbrains.com/idea/)" }
        }
    }

    @Test
    fun linkAfterHeader() {
        val source = """
            # Header
            [Some Doc](guide://)
            """.trimIndent()

        source assertEquals {
            + header1 { "Header" }
            + link { "[Some Doc](guide://)" }
        }
    }


    @Test
    fun imageLink() {
        val source = "![IntelliJ IDEA](https://www.jetbrains.com/idea/)"

        source assertEquals {
            + image { "[IntelliJ IDEA](https://www.jetbrains.com/idea/)" }
        }
    }

}
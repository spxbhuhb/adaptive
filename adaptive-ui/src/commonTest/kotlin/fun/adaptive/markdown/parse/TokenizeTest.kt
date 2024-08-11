/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.markdown.parse

import kotlin.test.Test
import kotlin.test.assertEquals

class TokenizeTest {

    @Test
    fun header1() {
        val source = "# Header"
        val expectedTokens = listOf(
            MarkdownToken(MarkdownTokenType.Header, "#"),
            MarkdownToken(MarkdownTokenType.Text, "Header")
        )
        val actualTokens = tokenize(source)
        assertEquals(expectedTokens, actualTokens)
    }

    @Test
    fun header4() {
        val source = "#### Header"
        val expectedTokens = listOf(
            MarkdownToken(MarkdownTokenType.Header, "####"),
            MarkdownToken(MarkdownTokenType.Text, "Header")
        )
        val actualTokens = tokenize(source)
        assertEquals(expectedTokens, actualTokens)
    }

    @Test
    fun headers() {
        val source = """
            # Header
            ## Header 2
        """.trimIndent()
        val expectedTokens = listOf(
            MarkdownToken(MarkdownTokenType.Header, "#"),
            MarkdownToken(MarkdownTokenType.Text, "Header"),
            MarkdownToken(MarkdownTokenType.NewLine, ""),
            MarkdownToken(MarkdownTokenType.Header, "##"),
            MarkdownToken(MarkdownTokenType.Text, "Header 2"),
        )
        val actualTokens = tokenize(source)
        assertEquals(expectedTokens, actualTokens)
    }

    @Test
    fun headerNoSpace() {
        val source = "#Header"
        val expectedTokens = listOf(
            MarkdownToken(MarkdownTokenType.Header, "#"),
            MarkdownToken(MarkdownTokenType.Text, "Header")
        )
        val actualTokens = tokenize(source)
        assertEquals(expectedTokens, actualTokens)
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

        val expectedTokens = listOf(
            MarkdownToken(MarkdownTokenType.BulletList, "*"),
            MarkdownToken(MarkdownTokenType.Text, "item 1"),
            MarkdownToken(MarkdownTokenType.NewLine, ""),
            MarkdownToken(MarkdownTokenType.Spaces, "  "),
            MarkdownToken(MarkdownTokenType.BulletList, "*"),
            MarkdownToken(MarkdownTokenType.Text, "item 1.1"),
            MarkdownToken(MarkdownTokenType.NewLine, ""),
            MarkdownToken(MarkdownTokenType.Spaces, "      "),
            MarkdownToken(MarkdownTokenType.BulletList, "*"),
            MarkdownToken(MarkdownTokenType.Text, "item 1.1.1"),
            MarkdownToken(MarkdownTokenType.NewLine, ""),
            MarkdownToken(MarkdownTokenType.Spaces, "  "),
            MarkdownToken(MarkdownTokenType.BulletList, "*"),
            MarkdownToken(MarkdownTokenType.Text, "item 1.2"),
            MarkdownToken(MarkdownTokenType.NewLine, ""),
            MarkdownToken(MarkdownTokenType.BulletList, "*"),
            MarkdownToken(MarkdownTokenType.Text, "item 2")
        )
        val actualTokens = tokenize(source)
        assertEquals(expectedTokens, actualTokens)
    }

    @Test
    fun numberList() {
        val source = """
            1. item 1
              1. item 1.1
                  1. item 1.1.1
              2. item 1.2
            2. item 2
        """.trimIndent()

        val expectedTokens = listOf(
            MarkdownToken(MarkdownTokenType.NumberedList, "1"),
            MarkdownToken(MarkdownTokenType.Text, "item 1"),
            MarkdownToken(MarkdownTokenType.NewLine, ""),
            MarkdownToken(MarkdownTokenType.Spaces, "  "),
            MarkdownToken(MarkdownTokenType.NumberedList, "1"),
            MarkdownToken(MarkdownTokenType.Text, "item 1.1"),
            MarkdownToken(MarkdownTokenType.NewLine, ""),
            MarkdownToken(MarkdownTokenType.Spaces, "      "),
            MarkdownToken(MarkdownTokenType.NumberedList, "1"),
            MarkdownToken(MarkdownTokenType.Text, "item 1.1.1"),
            MarkdownToken(MarkdownTokenType.NewLine, ""),
            MarkdownToken(MarkdownTokenType.Spaces, "  "),
            MarkdownToken(MarkdownTokenType.NumberedList, "2"),
            MarkdownToken(MarkdownTokenType.Text, "item 1.2"),
            MarkdownToken(MarkdownTokenType.NewLine, ""),
            MarkdownToken(MarkdownTokenType.NumberedList, "2"),
            MarkdownToken(MarkdownTokenType.Text, "item 2")
        )
        val actualTokens = tokenize(source)
        assertEquals(expectedTokens, actualTokens)
    }

    @Test
    fun codeFence() {
        val source = "```\ncode\n```"
        val expectedTokens = listOf(
            MarkdownToken(MarkdownTokenType.CodeFence, "code")
        )
        val actualTokens = tokenize(source)
        assertEquals(expectedTokens, actualTokens)
    }

    @Test
    fun codeFenceWithLanguage() {
        val source = "```text\ncode\n```"
        val expectedTokens = listOf(
            MarkdownToken(MarkdownTokenType.CodeLanguage, "text"),
            MarkdownToken(MarkdownTokenType.CodeFence, "code")
        )
        val actualTokens = tokenize(source)
        assertEquals(expectedTokens, actualTokens)
    }

    @Test
    fun boldText() {
        val source = "**bold text**"
        val expectedTokens = listOf(
            MarkdownToken(MarkdownTokenType.Asterisks, "**"),
            MarkdownToken(MarkdownTokenType.Text, "bold text"),
            MarkdownToken(MarkdownTokenType.Asterisks, "**")
        )
        val actualTokens = tokenize(source)
        assertEquals(expectedTokens, actualTokens)
    }

    @Test
    fun quote() {
        val source = ">This is a quote\n>and another quote"
        val expectedResult = listOf(
            Pair(MarkdownTokenType.Quote, "This is a quote\nand another quote")
        )

        val result = tokenize(source).map { Pair(it.type, it.text) }

        assertEquals(expectedResult, result)
    }

    @Test
    fun quotes() {
        val source = ">This is a quote\n> > and an inner quote"
        val expectedResult = listOf(
            Pair(MarkdownTokenType.Quote, "This is a quote\n > and an inner quote")
        )

        val result = tokenize(source).map { Pair(it.type, it.text) }

        assertEquals(expectedResult, result)
    }

    @Test
    fun asterisks() {
        val source = "*test*"
        val tokens = tokenize(source)
        assertEquals(3, tokens.size)
        assertEquals(MarkdownTokenType.Asterisks, tokens[0].type)
        assertEquals("*", tokens[0].text)
        assertEquals(MarkdownTokenType.Text, tokens[1].type)
        assertEquals("test", tokens[1].text)
        assertEquals(MarkdownTokenType.Asterisks, tokens[2].type)
        assertEquals("*", tokens[2].text)
    }

    @Test
    fun underscores() {
        val source = "_test_"
        val tokens = tokenize(source)
        assertEquals(3, tokens.size)
        assertEquals(MarkdownTokenType.Underscores, tokens[0].type)
        assertEquals("_", tokens[0].text)
        assertEquals(MarkdownTokenType.Text, tokens[1].type)
        assertEquals("test", tokens[1].text)
        assertEquals(MarkdownTokenType.Underscores, tokens[2].type)
        assertEquals("_", tokens[2].text)
    }

    @Test
    fun hyphens() {
        val source = "-test-"
        val tokens = tokenize(source)
        assertEquals(3, tokens.size)
        assertEquals(MarkdownTokenType.Hyphens, tokens[0].type)
        assertEquals("-", tokens[0].text)
        assertEquals(MarkdownTokenType.Text, tokens[1].type)
        assertEquals("test", tokens[1].text)
        assertEquals(MarkdownTokenType.Hyphens, tokens[2].type)
        assertEquals("-", tokens[2].text)
    }

    @Test
    fun codeSpan() {
        val input = "`println()`"
        val expected = listOf(
            MarkdownToken(MarkdownTokenType.CodeSpan, "println()")
        )

        val actual = tokenize(input)
        assertEquals(expected, actual)
    }

    @Test
    fun crazyCodeSpan() {
        val input = "`` ` ``"
        val expected = listOf(
            MarkdownToken(MarkdownTokenType.CodeSpan, "`")
        )

        val actual = tokenize(input)
        assertEquals(expected, actual)
    }

    @Test
    fun inlineLink() {
        val input = "[IntelliJ IDEA](https://www.jetbrains.com/idea/)"
        val expected = listOf(
            MarkdownToken(MarkdownTokenType.InlineLink, "[IntelliJ IDEA](https://www.jetbrains.com/idea/)")
        )

        val actual = tokenize(input)
        assertEquals(expected, actual)
    }

    @Test
    fun referenceLink() {
        val input = "[Unbelievable][1]\n" +
            "[1]: https://example.com/nothing"
        val expected = listOf(
            MarkdownToken(MarkdownTokenType.ReferenceLink, "[Unbelievable][1]"),
            MarkdownToken(MarkdownTokenType.NewLine, ""),
            MarkdownToken(MarkdownTokenType.ReferenceDef, "[1]: https://example.com/nothing")
        )

        val actual = tokenize(input)
        assertEquals(expected, actual)
    }

    @Test
    fun referenceDef() {
        val input = "[1]: https://www.jetbrains.com/"
        val expected = listOf(
            MarkdownToken(MarkdownTokenType.ReferenceDef, "[1]: https://www.jetbrains.com/")
        )

        val actual = tokenize(input)
        assertEquals(expected, actual)
    }

    @Test
    fun longData() {
        // just to see that it does not crash for longer data
        tokenize(longData)
    }
}

val longData = """
    # Expect Fragments

    When an adaptive function has the `@AdaptiveExpect` annotation, the fragment is built by calling the `actualize`
    method of the adapter, which in turn uses the `fragmentFactory` to get an instance of the fragment.

    ```kotlin
    import `fun`.adaptive.foundation.AdaptiveExpect
    import `fun`.adaptive.ui.common.commonUI

    @AdaptiveExpect(commonUI)
    fun text(text : String) {
        manualImplementation(text)
    }
    ```

    The parameter of the annotation is a namespace. The actual key to pass to `fragmentFactory.newInstance`
    is `$ namespace:$ shortClassName`.

    > [!IMPORTANT]
    > 
    > In contrast with Kotlin expect/actual, a missing implementation does not raise a compilation error for @AdaptiveExpect.
    > You will get a runtime exception if there is no implementation.
    >

    The main use case of `@AdaptiveExpect` is to have different implementations by adapter.

    The code above results in a `genBuild` like this:

    ```kotlin
    fun genBuild(parent: AdaptiveFragment<TestNode>, declarationIndex: Int): AdaptiveFragment<TestNode> {

        val fragment = when (declarationIndex) {
            0 -> adapter.actiualize("common:text", this, 0)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment 
    }
    ```
""".trimIndent()
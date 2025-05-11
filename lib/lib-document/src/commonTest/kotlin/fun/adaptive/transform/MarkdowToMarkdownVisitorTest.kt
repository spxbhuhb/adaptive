package `fun`.adaptive.transform

import `fun`.adaptive.markdown.compiler.parseInternal
import `fun`.adaptive.markdown.compiler.tokenizeInternal
import `fun`.adaptive.markdown.transform.MarkdownAstDumpVisitor.Companion.dump
import `fun`.adaptive.markdown.transform.MarkdownToMarkdownVisitor.Companion.toMarkdown
import kotlin.test.Test
import kotlin.test.assertEquals

class MarkdownToMarkdownVisitorTest {

    private fun String.parseAndRegenerate(): String {
        return parseInternal(tokenizeInternal(this)).toMarkdown()
    }

    @Test
    fun `test headers`() {
        val inputs = listOf(
            "# Header 1",
            "## Header 2",
            "### Header 3"
        )

        inputs.forEach { input ->
            assertEquals(input, input.parseAndRegenerate().trim())
        }
    }

    @Test
    fun `test inline formatting`() {
        val tests = mapOf(
            "**bold text**" to "**bold text**",
            "_italic text_" to "_italic text_",
            "`code`" to "`code`",
            "**_bold italic_**" to "_**bold italic**_",
            "[link text](https://example.com)" to "[link text](https://example.com)"
        )

        tests.forEach { (input, expected) ->
            assertEquals(expected, input.parseAndRegenerate().trim())
        }
    }

    @Test
    fun `test lists`() {
        val input = """
            * Item 1
            * Item 2
                * Nested item 1
                * Nested item 2
            * Item 3
        """.trimIndent()

        assertEquals(input, input.parseAndRegenerate().trim())
    }

    @Test
    fun `test numbered lists`() {
        val input = """
            1. First item
            2. Second item
                1. Nested item 1
                2. Nested item 2
            3. Third item
        """.trimIndent()

        val output = """
            1. First item
            1. Second item
                1. Nested item 1
                1. Nested item 2
            1. Third item
        """.trimIndent()

        assertEquals(output, input.parseAndRegenerate().trim())
    }

    @Test
    fun `test code fence`() {
        val input = """
            ```kotlin
            fun hello() {
                println("Hello, World!")
            }
            ```
        """.trimIndent()

        assertEquals(input, input.parseAndRegenerate().trim())
    }

    @Test
    fun `test blockquotes`() {
        val input = """
            > This is a quote
            > With multiple lines
            > And more content
        """.trimIndent()

        val output = "> This is a quote With multiple lines And more content"

        assertEquals(output, input.parseAndRegenerate().trim())
    }

    @Test
    fun `test nested blockquotes`() {
        val input = """
            > This is a quote
            > > With multiple lines
            > And more content
        """.trimIndent()

        assertEquals(input, input.parseAndRegenerate().trim())
    }

    @Test
    fun `test horizontal rule`() {
        val input = "---"
        assertEquals(input, input.parseAndRegenerate().trim())
    }

    @Test
    fun `test complex document`() {
        val input = """
            # Main Header
            
            This is a paragraph with **bold** and _italic_ text.
            
            ## Subheader
            
            * List item 1
                * Nested item with `code`
                * Another nested item
            * List item 2
            
            > This is a quote With [a link](https://example.com)
            
            ```kotlin
            fun test() {
                println("Hello")
            }
            ```
            
            ---
            
            Final paragraph.
        """.trimIndent()

        assertEquals(input, input.parseAndRegenerate().trim())
    }

    @Test
    fun `test paragraphs`() {
        val input = """
            First paragraph
            still first paragraph
            
            Second paragraph
            
            Third paragraph
        """.trimIndent()

        val output = """
            First paragraph still first paragraph

            Second paragraph

            Third paragraph
        """.trimIndent()

        assertEquals(output, input.parseAndRegenerate().trim())
    }

    @Test
    fun `test mixed nested lists`() {
        val input = """
            * Bullet item
                1. Numbered sub-item
                2. Another numbered sub-item
                    * Back to bullets
            * Another bullet item
        """.trimIndent()

        val output = """
            * Bullet item
                1. Numbered sub-item
                1. Another numbered sub-item
                    * Back to bullets
            * Another bullet item
        """.trimIndent()

        assertEquals(output, input.parseAndRegenerate().trim())
    }

    @Test
    fun `test reference links`() {
        val input = """
            [reference link][ref1]
            
            [another link][ref2]
            
            [ref1]: https://example.com
            
            [ref2]: https://example.org
        """.trimIndent()

        assertEquals(input, input.parseAndRegenerate().trim())
    }
}
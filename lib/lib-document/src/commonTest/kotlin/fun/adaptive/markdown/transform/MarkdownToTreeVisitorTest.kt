package `fun`.adaptive.markdown.transform

import `fun`.adaptive.markdown.compiler.parseInternal
import `fun`.adaptive.markdown.compiler.tokenizeInternal
import `fun`.adaptive.ui.tree.TreeItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MarkdownToTreeVisitorTest {

    @Test
    fun testTransformSimpleMarkdown() {
        val markdown = """
            # Header 1
            
            - Item 1
            - Item 2
            
            # Header 2
            
            - Item 3
            - Item 4
              - Subitem 1
              - Subitem 2
        """.trimIndent()

        val visitor = MarkdownToTreeVisitor(markdown)

        val result = visitor.transform()

        // Check that we have two root items (headers)
        assertEquals(2, result.size)

        // Check first header
        val header1 = result[0]
        assertEquals("Header 1", header1.title)
        assertEquals(2, header1.children.size)
        assertEquals("Item 1", header1.children[0].title)
        assertEquals("Item 2", header1.children[1].title)

        // Check second header
        val header2 = result[1]
        assertEquals("Header 2", header2.title)
        assertEquals(2, header2.children.size)
        assertEquals("Item 3", header2.children[0].title)
        assertEquals("Item 4", header2.children[1].title)

        // Check subitems
        val item4 = header2.children[1]
        assertEquals(2, item4.children.size)
        assertEquals("Subitem 1", item4.children[0].title)
        assertEquals("Subitem 2", item4.children[1].title)
    }

    @Test
    fun testTransformDocumentationMd() {
        val markdown = """
            # Introduction
            
            - [What is Adaptive](guide://)
            - [Getting started](guide://)
            
            # Application
            
            - [What is an application](guide://)
            
            # User interface
            
            - [Fragments and instructions](guides://)
            - [Layout system](guide://)
            - [Inputs](guide://)
              - [Int input](guide://)
              - [Text input](guide://)
            
            # Data models
            
            - [Adat classes](guide://)
            - [Value store](guide://)
            - [Serialization](guide://)
            
            # Communication
            
            - [Services](guide://)
            - [Ktor](guide://)
            
            # Impressum
            
            - [Credits](guide://)
            - [Motivation](guide://)
            - [License](guide://)
        """.trimIndent()

        val visitor = MarkdownToTreeVisitor(markdown) { title, link, parent ->
            TreeItem(
                icon = null,
                title = title,
                data = link,
                open = false,
                selected = false,
                parent = parent
            )
        }

        val result = visitor.transform()

        // Check that we have six root items (headers)
        assertEquals(6, result.size)

        // Check headers
        assertEquals("Introduction", result[0].title)
        assertEquals("Application", result[1].title)
        assertEquals("User interface", result[2].title)
        assertEquals("Data models", result[3].title)
        assertEquals("Communication", result[4].title)
        assertEquals("Impressum", result[5].title)

        // Check that each header has the correct number of children
        assertEquals(2, result[0].children.size) // Introduction
        assertEquals(1, result[1].children.size) // Application
        assertEquals(3, result[2].children.size) // User interface
        assertEquals(3, result[3].children.size) // Data models
        assertEquals(2, result[4].children.size) // Communication
        assertEquals(3, result[5].children.size) // Impressum

        // Check that the "Inputs" item has subitems
        val inputs = result[2].children[2]
        assertEquals("Inputs", inputs.title)
        assertEquals(2, inputs.children.size)
        assertEquals("Int input", inputs.children[0].title)
        assertEquals("Text input", inputs.children[1].title)
    }
}
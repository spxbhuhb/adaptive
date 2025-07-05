package `fun`.adaptive.markdown.transform

import `fun`.adaptive.document.visitor.DocDumpVisitor.Companion.dump
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * IMPORTANT remember [assertEqualsDump]
 */
class MarkdownToDocTransformTest {

    infix fun String.assertEqualsDump(expected: DocBuilder.() -> Unit) =
        assertEquals(
            DocBuilder().apply { expected() }.entries.joinToString("\n") { it.dump() },
            MarkdownToDocVisitor(this).transform().blocks.joinToString("\n") { it.dump() }
        )

    @Test
    fun bulletListOne() {
        val source = """
             * item 1
               * item 1.1
             * item 2
        """.trimIndent()

        source assertEqualsDump {
            + list {
                + item(1, label = "item 1") {
                    + item(1, 1, label = "item 1.1")
                }
                + item(2, label = "item 2")
            }
        }
    }

    @Test
    fun blockImage() {
        val text = "An image"
        val url = "https://github.com/spxbhuhb/adaptive-site-resources/blob/110801e15484cbe47db9396fc78827ab79408a82/images/deep-waters-50.jpg"
        val source = "![$text]($url)"

        source assertEqualsDump {
            + blockImage(url) { text }
        }
    }

}
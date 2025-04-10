package `fun`.adaptive.ui.fragment.paragraph

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.fragment.paragraph.model.Paragraph
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphItem
import `fun`.adaptive.ui.testing.AuiTestAdapter
import `fun`.adaptive.ui.testing.fragment.AdaptiveParagraph
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class ParagraphTest {

    @Test
    @JsName("empty_paragraph")
    fun `empty paragraph`() {
        val paragraph = createTestParagraph(emptyList())
        val rows = paragraph.computeRows(100.0)
        assertEquals(0, rows.size)
    }

    @Test
    @JsName("single_item_fits_perfectly")
    fun `test single row fits perfectly`() {
        val paragraph = createTestParagraph(
            listOf(
                mockParagraphItem(50.0, 20.0),
                mockParagraphItem(50.0, 20.0)
            )
        )

        val rows = paragraph.computeRows(100.0)
        assertEquals(1, rows.size)
        assertEquals(100.0, rows[0].width, 0.01)
        assertEquals(20.0, rows[0].height, 0.01)
    }

    @Test
    @JsName("items_wrap_to_new_row")
    fun `test items wrap to new row`() {
        val paragraph = createTestParagraph(
            listOf(
                mockParagraphItem(60.0, 20.0),
                mockParagraphItem(60.0, 20.0)
            )
        )

        val rows = paragraph.computeRows(100.0)
        assertEquals(2, rows.size)
        assertEquals(60.0, rows[0].width, 0.01)
        assertEquals(20.0, rows[0].height, 0.01)
        assertEquals(60.0, rows[1].width, 0.01)
        assertEquals(20.0, rows[1].height, 0.01)
    }

    @Test
    @JsName("single_item_wider_than_row")
    fun `test single item wider than row`() {
        val paragraph = createTestParagraph(
            listOf(
                mockParagraphItem(120.0, 30.0)
            )
        )

        val rows = paragraph.computeRows(100.0)
        assertEquals(1, rows.size)
        assertEquals(120.0, rows[0].width, 0.01)
        assertEquals(30.0, rows[0].height, 0.01)
    }

    @Test
    @JsName("multiple_items_in_single_row")
    fun `test multiple items in multiple rows`() {
        val paragraph = createTestParagraph(
            listOf(
                mockParagraphItem(40.0, 10.0),
                mockParagraphItem(40.0, 15.0),
                mockParagraphItem(30.0, 20.0),
                mockParagraphItem(60.0, 25.0)
            )
        )

        val rows = paragraph.computeRows(100.0)
        assertEquals(2, rows.size)
        assertEquals(80.0, rows[0].width, 0.01) // First row fits 2 items
        assertEquals(15.0, rows[0].height, 0.01) // Tallest item in row
        assertEquals(90.0, rows[1].width, 0.01) // Second row fits remaining items
        assertEquals(25.0, rows[1].height, 0.01) // Tallest item in row
    }

    private fun createTestParagraph(items: List<ParagraphItem>): AdaptiveParagraph {
        val adapter = AuiTestAdapter()
        val paragraph = AdaptiveParagraph(adapter, null, 0)

        paragraph.set(
            1,
            Paragraph(
                listOf(instructionsOf()),
                items
            )
        )

        return paragraph
    }

    private fun mockParagraphItem(width: Double, height: Double): ParagraphItem =
        TestParagraphItem(width, height)

}

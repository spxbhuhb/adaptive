package `fun`.adaptive.ui.fragment.paragraph

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.SizingProposal
import `fun`.adaptive.ui.fragment.layout.computeFinal
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphViewBackend
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphItem

abstract class AbstractParagraph<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AbstractAuiFragment<RT>(
    adapter, parent, declarationIndex, 2
) {

    val viewBackend: ParagraphViewBackend
        get() = get(1)

    var instructionSets = emptyList<AdaptiveInstructionGroup>()

    class Row(
        val items: List<ParagraphItem>,
        val width: Double = 0.0,
        val height: Double = 0.0
    )

    override fun computeLayout(
        proposal: SizingProposal
    ) {
        val rows = computeRows((renderData.layout?.instructedWidth ?: proposal.containerWidth) - renderData.surroundingHorizontal)

        val itemsWidth = rows.maxOfOrNull { it.width } ?: 0.0
        val itemsHeight = rows.sumOf { it.height }

        computeFinal(proposal, itemsWidth, itemsHeight)

        placeRows(rows)
    }

    fun computeRows(proposedWidth: Double): MutableList<Row> {

        val paragraph = this.viewBackend
        instructionSets = paragraph.instructionSets // so we avoid the continuous casting of state access

        val items = paragraph.items
        val rows = mutableListOf<Row>()
        var rowWidth = 0.0
        var rowHeight = 0.0
        var lastItem: ParagraphItem? = null
        var rowItems = mutableListOf<ParagraphItem>()

        for (item in items) {
            item.measure(this)

            val itemWidth = item.width
            val itemHeight = item.height

            // println("item: $item, width=$itemWidth, height=$itemHeight, surrounding=${item.surroundingHorizontal}")

            if (rowWidth + itemWidth > proposedWidth) {

                // drop white spaces at the end of the line
                if (item.isWhitespace) {
                    continue
                }

                // when the first item is longer than the row in itself
                // when it is not the very first item, rowItems cannot be empty
                if (rowItems.isEmpty()) {
                    rows += Row(listOf(item), itemWidth, itemHeight)
                    continue
                }

                rows += Row(rowItems, rowWidth, rowHeight)

                rowWidth = itemWidth
                rowHeight = itemHeight
                rowItems = mutableListOf(item)
                lastItem = item

                continue
            }

            // drop whitespace at the beginning of the line
            if (rowItems.isEmpty() && item.isWhitespace) {
                continue
            }

            val merged = lastItem?.merge(this, item)

            if (merged != null) {
                rowItems[rowItems.lastIndex] = merged
                rowWidth += merged.width - lastItem.width
                rowHeight = maxOf(rowHeight, merged.height)
                lastItem = merged
            } else {
                rowItems += item
                rowWidth += itemWidth
                rowHeight = maxOf(rowHeight, itemHeight)
                lastItem = item
            }
        }

        if (rowItems.isNotEmpty()) {
            rows += Row(rowItems, rowWidth, rowHeight)
        }

        return rows
    }

    /**
     * Place the rows in the actual receiver. This is a platform specific function.
     */
    abstract fun placeRows(rows: List<Row>)

    /**
     * Measure a text item. This is a platform specific function.
     */
    abstract fun measureText(item: ParagraphItem, text: String, instructionSetIndex: Int)

}
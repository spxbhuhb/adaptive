package `fun`.adaptive.ui.table

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.*
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.AbstractTheme
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

open class TableTheme(
    headerHeight : DPixel = 28.dp
) : AbstractTheme() {

    companion object {
        var default = TableTheme()
    }

    /**
     * Subtracted from the available width to calculate cell arrangement. It should be
     * in line with the surrounding from [itemContainer]. Technically, it would be
     * possible to calculate this, but I think it isn't worth the effort.
     */
    var arrangementWidthAdjustment = 2.0 + 16.0 // border, padding

    // ---------------------------------------------------------------------------------------
    // Header
    // ---------------------------------------------------------------------------------------

    var headerContainer = instructionsOf(
        paddingVertical { 4.dp },
        paddingHorizontal { 8.dp },
        gap { 16.dp } // FIXME cellbox gap vs. table gap
    )

    var headerCell = instructionsOf(
        height { headerHeight },
        maxWidth,
        paddingLeft { 8.dp },
        paddingRight { 4.dp },
        paddingVertical { 12.dp },
        cornerRadius { 6.dp },
        alignItems.center,
        fillStrategy.constrainReverse
    )

    var headerCellText = instructionsOf(
        noSelect,
        normalFont,
        maxWidth
    )

    var headerCellTextEndAligned = headerCellText + instructionsOf(
        alignSelf.endCenter
    )

    var headerActionContainer = instructionsOf(
        height { headerHeight },
        alignItems.endCenter
    )

    open fun sortIcon(cellDef : TableCellDef<*, *>, hover : Boolean) =
        when (cellDef.sorting) {
            Sorting.Ascending -> Graphics.arrow_drop_up
            Sorting.Descending -> Graphics.arrow_drop_down
            else -> if (hover && cellDef.sortable) Graphics.unfold_more else Graphics.empty
        }

    var resizeHandle = instructionsOf(
        maxHeight,
        width { 10.dp },
        margin { 2.dp }
    )

    var resizeHandleHover = resizeHandle + instructionsOf(
        border(colors.outline, 0.dp, 1.dp, 0.dp, 1.dp),
        cursor.colResize
    )

    // ---------------------------------------------------------------------------------------
    // Filter
    // ---------------------------------------------------------------------------------------

    var filterContainer = instructionsOf(
        paddingVertical { 8.dp } .. paddingHorizontal { 8.dp },
        alignItems.center
    )

    var filterInput = instructionsOf(
        width { 200.dp }
    )

    // ---------------------------------------------------------------------------------------
    // Content
    // ---------------------------------------------------------------------------------------

    var contentContainer = instructionsOf(
        maxHeight,
        verticalScroll,
        gap { 8.dp }
    )

    var itemContainer = instructionsOf(
        borders.outline .. cornerRadius { 4.dp } .. paddingVertical { 4.dp } .. paddingHorizontal { 8.dp },
        gap { 16.dp } // FIXME cellbox gap vs. table gap
    )

    var cellContainer = instructionsOf(
        height { 28.dp },
        maxWidth,
        paddingLeft { 8.dp },
        paddingTop { 3.dp }, // FIXME paddingTop because of text baseline
        alignItems.startCenter,
        alignSelf.startCenter // FIXME alignSelf because of text alignment confusion
    )

    var cellContainerEndAligned = cellContainer + instructionsOf(
        alignSelf.endCenter,
        paddingRight { 4.dp } // same as header cell paddingRight
    )

    var iconCellContainer = cellContainer + instructionsOf(
        paddingTop { 0.dp } // FIXME assuming 24.dp icon size
    )

    var statusCellContainer = cellContainer + instructionsOf(
        paddingTop { 0.dp },
        maxWidth,
        horizontalScroll,
        gap { 8.dp }
    )

    var actionsCellContainer = cellContainer + instructionsOf(
        paddingTop { 0.dp },
        maxWidth,
        horizontalScroll
    )

    // ---------------------------------------------------------------------------------------
    // Filter
    // ---------------------------------------------------------------------------------------

    var filterText = instructionsOf(
        width { 200.dp },
        focusFirst,
        inputPlaceholder { Strings.filterPlaceholder }
    )
}
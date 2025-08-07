package `fun`.adaptive.ui.table

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.empty
import `fun`.adaptive.ui.generated.resources.north
import `fun`.adaptive.ui.generated.resources.unfold_more
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.AbstractTheme
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

open class TableTheme(
    headerHeight: DPixel = 28.dp
) : AbstractTheme() {

    companion object {
        var default = TableTheme()
    }

    var headerCell = instructionsOf(
        height { headerHeight },
        paddingLeft { 4.dp },
        alignItems.center,
        spaceBetween,
        fillStrategy.constrainReverse
    )

    var headerCellText = instructionsOf(
        noSelect,
        normalFont
    )

    var headerActionContainer = instructionsOf(
        height { headerHeight },
        alignItems.center
    )

    open fun sortIcon(cellDef: TableCellDef<*,*>, hover: Boolean) =
        when (cellDef.sorting) {
            Sorting.Ascending -> Graphics.north
            Sorting.Descending -> Graphics.north
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

    /**
     * Subtracted from the available width to calculate cell arrangement. It should be
     * in line with the surrounding from [itemContainer]. Technically, it would be
     * possible to calculate this, but I think it isn't worth the effort.
     */
    var arrangementWidthAdjustment = 2.0 + 16.0 // border, padding

    var headerContainer = instructionsOf(
        paddingVertical { 4.dp } .. paddingHorizontal { 8.dp }
    )

    var itemContainer = instructionsOf(
        borders.outline .. cornerRadius { 4.dp } .. paddingVertical { 4.dp } .. paddingHorizontal { 8.dp }
    )
}
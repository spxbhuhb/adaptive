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
import `fun`.adaptive.ui.theme.colors

class TableTheme(
    headerHeight: DPixel = 28.dp
) : AbstractTheme() {

    companion object {
        var default = TableTheme()
    }

    val headerCell = instructionsOf(
        height { headerHeight },
        paddingLeft { 4.dp },
        alignItems.center,
        spaceBetween,
        fillStrategy.constrainReverse
    )

    val headerCellText = instructionsOf(
        noSelect,
        normalFont
    )

    val headerActionContainer = instructionsOf(
        height { headerHeight },
        alignItems.center
    )

    fun sortIcon(cellDef: TableCellDef<*,*>, hover: Boolean) =
        when (cellDef.sorting) {
            Sorting.Ascending -> Graphics.north
            Sorting.Descending -> Graphics.north
            else -> if (hover && cellDef.sortable) Graphics.unfold_more else Graphics.empty
        }

    val resizeHandle = instructionsOf(
        maxHeight,
        width { 10.dp },
        margin { 2.dp }
    )

    val resizeHandleHover = resizeHandle + instructionsOf(
        border(colors.outline, 0.dp, 1.dp, 0.dp, 1.dp),
        cursor.colResize
    )
}
package `fun`.adaptive.ui.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.nop
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseIconTheme
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.layout.AlignSelf
import `fun`.adaptive.ui.instruction.layout.Alignment
import `fun`.adaptive.ui.menu.itemActionsMenu
import `fun`.adaptive.ui.theme.backgrounds

@Adaptive
fun <ITEM> tableHeaderCell(
    cellDef : TableCellDef<ITEM, *>
) : AdaptiveFragment {

    val hover = hover()
    val theme = cellDef.table.tableTheme
    val observed = observe { cellDef }
    val highlight = if (hover) backgrounds.surfaceVariant else nop

    row {
        theme.headerCell .. highlight
        onClick { observed.table.sort(observed) }

        text(observed.label) .. (cellDef.headerInstructions?.invoke() ?: theme.headerCellText)

        row {
            theme.headerActionContainer

            if (observed.sortable && (observed.sorting != Sorting.None || hover)) {
                icon(
                    theme.sortIcon(observed, hover),
                    theme = denseIconTheme
                )
            }

//            if (observed.rowMenu.isNotEmpty()) {
//                itemActionsMenu(observed.rowMenu, iconTheme = denseIconTheme) {
//
//                }
//            }

//            if (observed.resizable) {
//                box {
//                    if (hover) theme.resizeHandleHover else theme.resizeHandle
//                }
//            }
        }

    }

    return fragment()
}
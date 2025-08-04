package `fun`.adaptive.ui.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseIconTheme
import `fun`.adaptive.ui.menu.itemActionsMenu

@Adaptive
fun <ITEM> tableHeaderCell(
    cellDef: TableCellDef<ITEM,*>
) : AdaptiveFragment {

    val hover = hover()
    val theme = cellDef.table.tableTheme
    val observed = observe { cellDef }

    row {
        //theme.headerCell .. width { observed.width }

        text(observed.label) .. theme.headerCellText .. maxWidth

        row {
            theme.headerActionContainer

            actionIcon(theme.sortIcon(observed, hover), theme = denseIconTheme)

            if (observed.rowMenu.isNotEmpty()) {
                itemActionsMenu(observed.rowMenu, iconTheme = denseIconTheme) {

                }
            }

            box {
                if (hover) theme.resizeHandleHover else theme.resizeHandle
            }
        }
    }

    return fragment()
}
package `fun`.adaptive.ui.table.renderer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.generated.resources.check
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.icon.noContainerIconTheme
import `fun`.adaptive.ui.table.TableCellDef

@Adaptive
fun <ITEM> tableCellBoolean(cellDef : TableCellDef<ITEM, Boolean?>, item : ITEM) : AdaptiveFragment {

    box {
        cellDef.table.tableTheme.iconCellContainerCentered

        if (cellDef.getFun(item) == true) {
            icon(
                Graphics.check,
                theme = noContainerIconTheme
            )
        }
    }

    return fragment()
}
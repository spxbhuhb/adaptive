package `fun`.adaptive.ui.table.renderer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.generated.resources.empty
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.icon.noContainerIconTheme
import `fun`.adaptive.ui.table.TableCellDef

@Adaptive
fun <ITEM> tableCellIcon(cellDef: TableCellDef<ITEM, GraphicsResourceSet?>, item: ITEM) : AdaptiveFragment {
    box {
        cellDef.table.tableTheme.iconCellContainer

        icon(
            cellDef.getFun(item) ?: Graphics.empty,
            theme = noContainerIconTheme
        ) .. cellDef.instructions(item)
    }
    return fragment()
}
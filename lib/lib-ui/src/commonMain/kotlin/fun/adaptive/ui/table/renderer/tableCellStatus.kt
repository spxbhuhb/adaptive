package `fun`.adaptive.ui.table.renderer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResource
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.horizontalScroll
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.badge.badge
import `fun`.adaptive.ui.generated.resources.empty
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.table.TableCellDef
import `fun`.adaptive.value.AvStatus

@Adaptive
fun <ITEM> tableCellStatus(cellDef: TableCellDef<ITEM, Set<AvStatus>?>, item: ITEM) : AdaptiveFragment {
    row {
        cellDef.table.tableTheme.statusCellContainer

        for (status in cellDef.getFun(item) ?: emptySet()) {
            badge(status, useSeverity = true)
        }
    }
    return fragment()
}
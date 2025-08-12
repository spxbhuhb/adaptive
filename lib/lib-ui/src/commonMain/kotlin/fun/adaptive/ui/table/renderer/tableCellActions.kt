package `fun`.adaptive.ui.table.renderer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.icon.ActionIconRowBackend
import `fun`.adaptive.ui.icon.actionIconRow
import `fun`.adaptive.ui.table.TableCellDef

@Adaptive
fun <ITEM> tableCellActions(cellDef: TableCellDef<ITEM, ActionIconRowBackend<Any>?>, item: ITEM) : AdaptiveFragment {
    actionIconRow(
        cellDef.getFun(item) ?: ActionIconRowBackend(emptyList(), emptyList()) { }
    ) .. cellDef.table.tableTheme.actionsCellContainer
    return fragment()
}
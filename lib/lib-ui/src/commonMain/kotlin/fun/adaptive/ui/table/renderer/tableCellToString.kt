package `fun`.adaptive.ui.table.renderer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.table.TableCellDef

@Adaptive
fun <ITEM,CELL_DATA> tableCellToString(cellDef: TableCellDef<ITEM,CELL_DATA>, item: ITEM) : AdaptiveFragment {
    text(cellDef.getFun(item)?.toString() ?: "") .. cellDef.instructions .. maxWidth
    return fragment()
}
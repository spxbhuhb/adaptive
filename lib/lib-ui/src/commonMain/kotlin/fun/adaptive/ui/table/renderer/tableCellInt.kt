package `fun`.adaptive.ui.table.renderer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.table.TableCellDef
import `fun`.adaptive.utility.format

@Adaptive
fun <ITEM> tableCellInt(cellDef: TableCellDef<ITEM, Int?>, item: ITEM) : AdaptiveFragment {
    text(cellDef.getFun(item) ?: "") .. cellDef.instructions .. maxWidth
    return fragment()
}
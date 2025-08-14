package `fun`.adaptive.ui.table.renderer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.table.TableCellDef
import `fun`.adaptive.utility.format

@Adaptive
fun <ITEM> tableCellDouble(cellDef : TableCellDef<ITEM, Double?>, item : ITEM) : AdaptiveFragment {
    text(cellDef.getFun(item)?.format(cellDef.decimals, unit = cellDef.unit)) .. cellDef.instructions(item) .. maxWidth
    return fragment()
}
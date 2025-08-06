package `fun`.adaptive.ui.table.renderer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResource
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.generated.resources.empty
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.table.TableCellDef

@Adaptive
fun <ITEM> tableCellIcon(cellDef: TableCellDef<ITEM, GraphicsResourceSet?>, item: ITEM) : AdaptiveFragment {
    icon(cellDef.getFun(item) ?: Graphics.empty) .. cellDef.instructions
    return fragment()
}
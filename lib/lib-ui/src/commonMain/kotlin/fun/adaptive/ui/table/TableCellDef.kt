package `fun`.adaptive.ui.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.layout.GridTrack
import `fun`.adaptive.ui.menu.MenuItemBase
import kotlin.properties.Delegates.observable

class TableCellDef<ITEM, CELL_DATA>(
    val table: TableViewBackend<ITEM>,
    label: String,
    width: GridTrack,
    minWidth: DPixel,
    instructions: AdaptiveInstructionGroup,
    val getFun: (ITEM) -> CELL_DATA,
    contentFun: @Adaptive (TableCellDef<ITEM, CELL_DATA>, ITEM) -> Any
) : SelfObservable<TableCellDef<ITEM, CELL_DATA>>() {

    var label by observable(label, ::notify)
    var width by observable(width, ::notify)
    var minWidth by observable(minWidth, ::notify)

    var visible by observable(true, ::notify)
    var sortable by observable(true, ::notify)
    var resizable by observable(true, ::notify)

    var rowMenu by observable(emptyList<MenuItemBase<Any>>(), ::notify)

    var instructions by observable(instructions, ::notify)
    var contentFun by observable(contentFun, ::notify)

    var sorting by observable(Sorting.None, ::notify)


}
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
    var compareFunction : (ITEM, ITEM) -> Int = ::defaultCompareFunction

    var sortOrder = 0

    fun defaultCompareFunction(item1 : ITEM, item2 : ITEM) : Int {
        val value1 = getFun(item1)
        val value2 = getFun(item2)

        var result = 0

        // Compare the values if they're comparable
        if (value1 is Comparable<*> && value2 is Comparable<*>) {
            try {
                @Suppress("UNCHECKED_CAST")
                result = (value1 as? Comparable<Any>)?.compareTo(value2) ?: 0
                if (sorting == Sorting.Descending) {
                    result = -result
                }
            } catch (e: ClassCastException) {
                // If values can't be compared, continue to the next cell
            }
        }

        return result
    }
}
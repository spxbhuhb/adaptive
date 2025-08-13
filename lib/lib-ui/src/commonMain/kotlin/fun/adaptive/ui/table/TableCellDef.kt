package `fun`.adaptive.ui.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.runtime.application
import `fun`.adaptive.runtime.hasRole
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.layout.GridTrack
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.utility.UUID
import kotlin.properties.Delegates.observable

class TableCellDef<ITEM, CELL_DATA>(
    val table : TableViewBackend<ITEM>,
    label : String,
    width : GridTrack,
    minWidth : DPixel,
    instructions : AdaptiveInstructionGroup,
    val getFun : (ITEM) -> CELL_DATA,
    val matchFun : ((CELL_DATA, filterText : String) -> Boolean)? = null,
    contentFun : @Adaptive (TableCellDef<ITEM, CELL_DATA>, ITEM) -> Any
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

    // TODO move decimals and unit to numeric cell somehow (not trivial as function references can't have a type)
    var decimals : Int = 2
    var unit : String? = null

    var sorting by observable(Sorting.None, ::notify)
    var compareFunction : (ITEM, ITEM) -> Int = ::defaultCompareFunction

    var sortOrder = 0

    var supportsTextFilter = true

    var roleUuid : UUID<*>? = null

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
                    result = - result
                }
            } catch (e : ClassCastException) {
                // If values can't be compared, continue to the next cell
            }
        }

        return result
    }

    fun matches(item : ITEM, filterText : String) : Boolean {
        if (! supportsTextFilter) return false

        val cellData = getFun(item)

        if (matchFun != null) return matchFun.invoke(cellData, filterText)
        return cellData.toString().contains(filterText, ignoreCase = true)
    }

    fun <T> takeIfRole(fragment : AdaptiveFragment, block : () -> T) : T? {
        return if (roleUuid == null || fragment.hasRole(roleUuid !!)) block() else null
    }
}
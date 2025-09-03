package `fun`.adaptive.ui.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.runtime.hasRole
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.layout.GridTrack
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.utility.UUID

data class TableCellDef<ITEM, CELL_DATA>(

    val table : TableViewBackend<ITEM>,
    val label : String,
    val width : GridTrack,
    val minWidth : DPixel,
    val instructions : (ITEM) -> AdaptiveInstructionGroup,
    val getFun : (ITEM) -> CELL_DATA,
    val matchFun : ((CELL_DATA, filterText : String) -> Boolean)? = null,
    val contentFun : @Adaptive (TableCellDef<ITEM, CELL_DATA>, ITEM) -> Any,

    /**
     * For linked columns (when some data the column depends on loads asynchronously),
     * the revision is used to determine if the cell should be updated.
     */
    val revision : Int = 0,

    /**
     * Set the key during the table construction to find the cell by key later.
     * Useful for linked columns (when some data the column depends on loads asynchronously).
     */
    val key : String? = null,
    val visible : Boolean = true,
    val sortable : Boolean = true,
    val resizable : Boolean = true,

    val rowMenu : List<MenuItemBase<Any>> = emptyList(),

    val headerInstructions : (() -> AdaptiveInstructionGroup)? = null,

    // TODO move decimals and unit to numeric cell somehow (not trivial as function references can't have a type)
    val decimals : Int = 2,
    val unit : String? = null,

    val sorting : Sorting = Sorting.None,

    val sortOrder : Int = 0,

    val supportsTextFilter : Boolean = true,

    val role : UUID<*>? = null,
    val group : TableCellGroupDef? = null

) : SelfObservable<TableCellDef<ITEM, CELL_DATA>>() {

    val compareFunction : (ITEM, ITEM) -> Int = ::defaultCompareFunction

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
        return if (role == null || fragment.hasRole(role)) block() else null
    }
}
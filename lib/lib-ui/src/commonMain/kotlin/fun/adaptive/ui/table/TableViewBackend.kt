package `fun`.adaptive.ui.table

import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Gap

/**
 * @property    allItems         All the items this table has, including invisible (filtered out).
 * @property    filteredItems    The items that match the current state of the filter.
 * @property    viewportItems    Items that are shown in the current viewport.
 */
class TableViewBackend<ITEM> : SelfObservable<TableViewBackend<ITEM>>() {

    val cells: MutableList<TableCellDef<ITEM, *>> = mutableListOf()

    val tableTheme = TableTheme.default

    var allItems: MutableList<TableItem<ITEM>>? = null

    var filteredItems: List<TableItem<ITEM>>? = null

    var viewportItems: List<TableItem<ITEM>>? = null

    var gap : Gap = Gap(0.dp, 0.dp)

}
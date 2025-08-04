package `fun`.adaptive.ui.table

import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.ui.table.renderer.tableItemToString

/**
 * @property    allItems         All the items this table has, including invisible (filtered out).
 * @property    filteredItems    The items that match the current state of the filter.
 * @property    viewportItems    Items that are shown in the current viewport.
 */
class TableViewBackend<ITEM> : SelfObservable<TableViewBackend<ITEM>>() {

    val cells: MutableList<TableCellDef<ITEM, Any>> = mutableListOf()

    val tableTheme = TableTheme.default

    var allItems: MutableList<TableItem<ITEM>>? = null

    var filteredItems: List<TableItem<ITEM>>? = null

    var viewportItems: List<TableItem<ITEM>>? = null

    fun <CELL_DATA : Any> cell(
        label: String,
        width: DPixel,
        getFun: (ITEM) -> CELL_DATA,
        menu: List<MenuItemBase<Any>>? = null,
        buildFun: TableCellDef<ITEM, CELL_DATA>.() -> Unit = {}
    ) {
        cells += TableCellDef(
            table = this,
            label = label,
            width = width,
            instructions = emptyInstructions,
            getFun = getFun
        ).also { cell ->
            if (menu != null) cell.rowMenu = menu
        }.apply(buildFun) as TableCellDef<ITEM, Any>
    }

    fun intCell(buildFun : TableCellDef<ITEM,Int>.() -> Unit) {

    }

    companion object {
        fun <ITEM> tableViewBackend(buildFun: TableViewBackend<ITEM>.() -> Unit): TableViewBackend<ITEM> =
            TableViewBackend<ITEM>().apply(buildFun)
    }
}
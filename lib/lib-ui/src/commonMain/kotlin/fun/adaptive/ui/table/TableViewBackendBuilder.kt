package `fun`.adaptive.ui.table

import `fun`.adaptive.ui.table.renderer.tableItemToString

class TableViewBackendBuilder<ITEM_TYPE> {

    val cells = mutableListOf<TableCellDefBuilder<ITEM_TYPE, *>>()

    var items : List<ITEM_TYPE> = emptyList()

    fun intCell(buildFun: TableCellDefBuilder<ITEM_TYPE, Int>.() -> Unit) {
        cells += TableCellDefBuilder<ITEM_TYPE, Int>().also {
            //it.content = ::tableItemToString
            buildFun(it)
        }
    }

    fun toBackend() : TableViewBackend<ITEM_TYPE> {
        TableViewBackend<ITEM_TYPE>().also { table ->
            table.cells += cells.map { cell -> cell.toTableCellDef(table) }
            table.allItems = items.map { TableItem(it) }.toMutableList()
            table.viewportItems = table.allItems
            return table
        }
    }

    companion object {
        fun <ITEM_TYPE> tableBackend(buildFun : TableViewBackendBuilder<ITEM_TYPE>.() -> Unit) : TableViewBackend<ITEM_TYPE> {
            return TableViewBackendBuilder<ITEM_TYPE>().apply(buildFun).toBackend()
        }
    }

}
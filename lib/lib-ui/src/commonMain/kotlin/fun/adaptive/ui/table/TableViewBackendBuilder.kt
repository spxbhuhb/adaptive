package `fun`.adaptive.ui.table

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.table.renderer.tableCellDouble
import `fun`.adaptive.ui.table.renderer.tableCellIcon
import `fun`.adaptive.ui.table.renderer.tableCellToString

class TableViewBackendBuilder<ITEM_TYPE> {

    val cells = mutableListOf<TableCellDefBuilder<ITEM_TYPE, *>>()

    var items : List<ITEM_TYPE> = emptyList()

    fun stringCell(buildFun: TableCellDefBuilder<ITEM_TYPE, String?>.() -> Unit) {
        cells += TableCellDefBuilder<ITEM_TYPE, String?>().also {
            it.content = ::tableCellToString
            buildFun(it)
        }
    }

    fun intCell(buildFun: TableCellDefBuilder<ITEM_TYPE, Int>.() -> Unit) {
        cells += TableCellDefBuilder<ITEM_TYPE, Int>().also {
            it.content = ::tableCellToString
            buildFun(it)
        }
    }

    fun doubleCell(buildFun: TableCellDefBuilder<ITEM_TYPE, Double?>.() -> Unit) {
        cells += TableCellDefBuilder<ITEM_TYPE, Double?>().also {
            it.content = ::tableCellDouble
            buildFun(it)
        }
    }

    fun iconCell(buildFun: TableCellDefBuilder<ITEM_TYPE, GraphicsResourceSet?>.() -> Unit) {
        cells += TableCellDefBuilder<ITEM_TYPE, GraphicsResourceSet?>().also {
            it.content = ::tableCellIcon
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
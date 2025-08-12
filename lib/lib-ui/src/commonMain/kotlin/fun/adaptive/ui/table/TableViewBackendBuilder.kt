package `fun`.adaptive.ui.table

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.icon.ActionIconRowBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Gap
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.ui.table.renderer.tableCellActions
import `fun`.adaptive.ui.table.renderer.tableCellDouble
import `fun`.adaptive.ui.table.renderer.tableCellTimeAgo
import `fun`.adaptive.ui.table.renderer.tableCellIcon
import `fun`.adaptive.ui.table.renderer.tableCellInt
import `fun`.adaptive.ui.table.renderer.tableCellStatus
import `fun`.adaptive.ui.table.renderer.tableCellToString
import `fun`.adaptive.value.AvStatus
import kotlin.time.Instant

class TableViewBackendBuilder<ITEM_TYPE> {

    val cells = mutableListOf<TableCellDefBuilder<ITEM_TYPE, *>>()

    var items : List<ITEM_TYPE> = emptyList()

    val gap : Gap = Gap(16.dp, 16.dp)

    fun stringCell(buildFun: TableCellDefBuilder<ITEM_TYPE, String?>.() -> Unit) {
        cells += TableCellDefBuilder<ITEM_TYPE, String?>().also {
            it.content = ::tableCellToString
            buildFun(it)
        }
    }

    fun intCell(buildFun: TableCellDefBuilder<ITEM_TYPE, Int?>.() -> Unit) {
        cells += TableCellDefBuilder<ITEM_TYPE, Int?>().also {
            it.content = ::tableCellInt
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
            it.resizable = false
            it.sortable = false
            buildFun(it)
        }
    }

    fun statusCell(buildFun: TableCellDefBuilder<ITEM_TYPE, Set<AvStatus>?>.() -> Unit) {
        cells += TableCellDefBuilder<ITEM_TYPE, Set<AvStatus>?>().also {
            it.content = ::tableCellStatus
            it.resizable = false
            it.sortable = false
            buildFun(it)
        }
    }

    fun timeAgoCell(buildFun: TableCellDefBuilder<ITEM_TYPE, Instant?>.() -> Unit) {
        cells += TableCellDefBuilder<ITEM_TYPE, Instant?>().also {
            it.content = ::tableCellTimeAgo
            it.resizable = false
            it.sortable = false
            buildFun(it)
        }
    }

    fun actionsCell(buildFun: TableCellDefBuilder<ITEM_TYPE, ActionIconRowBackend<Any>?>.() -> Unit) {
        cells += TableCellDefBuilder<ITEM_TYPE, ActionIconRowBackend<Any>?>().also {
            it.content = ::tableCellActions
            it.sortable = false
            it.resizable = false
            buildFun(it)
        }
    }

//  FIXME Custom table cell doesn't work because of some IR/js linking problem
//    fun <T> customCell(buildFun: TableCellDefBuilder<ITEM_TYPE, T?>.() -> Unit) {
//        cells += TableCellDefBuilder<ITEM_TYPE, T?>().also {
//            buildFun(it)
//        }
//    }

    fun toBackend() : TableViewBackend<ITEM_TYPE> {
        TableViewBackend<ITEM_TYPE>().also { table ->
            table.cells += cells.map { cell -> cell.toTableCellDef(table) }
            // FIXME table items to mutable list, might be bad performance-wise to to it by default
            table.allItems = items.map { TableItem(it) }.toMutableList()
            table.viewportItems = table.allItems
            table.gap = gap
            return table
        }
    }

    companion object {
        fun <ITEM_TYPE> tableBackend(buildFun : TableViewBackendBuilder<ITEM_TYPE>.() -> Unit) : TableViewBackend<ITEM_TYPE> {
            return TableViewBackendBuilder<ITEM_TYPE>().apply(buildFun).toBackend()
        }
    }

}
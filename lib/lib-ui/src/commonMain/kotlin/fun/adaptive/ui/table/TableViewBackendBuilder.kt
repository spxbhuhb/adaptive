package `fun`.adaptive.ui.table

import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.icon.ActionIconRowBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Gap
import `fun`.adaptive.ui.table.renderer.*
import `fun`.adaptive.value.AvStatus
import kotlin.time.Instant

class TableViewBackendBuilder<ITEM_TYPE>(
    val theme : TableTheme = TableTheme.default
) {

    val cells = mutableListOf<TableCellDefBuilder<ITEM_TYPE, *>>()

    var items : List<ITEM_TYPE> = emptyList()

    val gap : Gap = Gap(16.dp, 16.dp)

    var numericWidth = 140.dp

    var cellGroups : List<TableCellGroupDef> = emptyList()

    var onDoubleClick : ((ITEM_TYPE) -> Unit)? = null

    fun cellGroup(label : String, priority : Int) : TableCellGroupDef {
        return TableCellGroupDef(label, priority).also { cellGroups += it }
    }

    fun booleanCell(buildFun: TableCellDefBuilder<ITEM_TYPE, Boolean?>.() -> Unit) {
        cells += TableCellDefBuilder<ITEM_TYPE, Boolean?>().also {
            it.content = ::tableCellBoolean
            it.width = numericWidth
            it.minWidth = numericWidth
            it.headerInstructions = { theme.headerCellTextCenterAligned }
            buildFun(it)
        }
    }

    fun doubleCell(buildFun: TableCellDefBuilder<ITEM_TYPE, Double?>.() -> Unit) {
        cells += TableCellDefBuilder<ITEM_TYPE, Double?>().also {
            it.content = ::tableCellDouble
            it.width = numericWidth
            it.minWidth = numericWidth
            it.headerInstructions = { theme.headerCellTextEndAligned }
            it.instructions = { theme.cellContainerEndAligned }
            buildFun(it)
        }
    }

    fun intCell(buildFun: TableCellDefBuilder<ITEM_TYPE, Int?>.() -> Unit) {
        cells += TableCellDefBuilder<ITEM_TYPE, Int?>().also {
            it.content = ::tableCellInt
            it.width = numericWidth
            it.minWidth = numericWidth
            it.headerInstructions = { theme.headerCellTextEndAligned }
            it.instructions = { theme.cellContainerEndAligned }
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
            it.matchFun = { data, filterText -> data?.any { s -> filterText in s } == true }
            buildFun(it)
        }
    }

    fun stringCell(buildFun: TableCellDefBuilder<ITEM_TYPE, String?>.() -> Unit) {
        cells += TableCellDefBuilder<ITEM_TYPE, String?>().also {
            it.content = ::tableCellToString
            buildFun(it)
        }
    }

    fun timeAgoCell(buildFun: TableCellDefBuilder<ITEM_TYPE, Instant?>.() -> Unit) {
        cells += TableCellDefBuilder<ITEM_TYPE, Instant?>().also {
            it.content = ::tableCellTimeAgo
            it.resizable = false
            it.sortable = true
            it.supportsTextFilter = false
            buildFun(it)
        }
    }

    fun timeAgoCell10(buildFun: TableCellDefBuilder<ITEM_TYPE, Instant?>.() -> Unit) {
        cells += TableCellDefBuilder<ITEM_TYPE, Instant?>().also {
            it.content = ::tableCellTimeAgo10
            it.resizable = false
            it.sortable = true
            it.supportsTextFilter = false
            buildFun(it)
        }
    }

    fun <T> actionsCell(buildFun: TableCellDefBuilder<ITEM_TYPE, ActionIconRowBackend<T>?>.() -> Unit) {
        cells += TableCellDefBuilder<ITEM_TYPE, ActionIconRowBackend<T>?>().also {
            it.content = ::tableCellActions
            it.sortable = false
            it.resizable = false
            it.supportsTextFilter = false
            it.minWidth = 40.dp
            it.width = 40.dp
            buildFun(it)
        }
    }

//  FIXME Custom table cell doesn't work because of some IR/js linking problem
//    fun <T> customCell(buildFun: TableCellDefBuilder<ITEM_TYPE, T?>.() -> Unit) {
//        cells += TableCellDefBuilder<ITEM_TYPE, T?>().also {
//            buildFun(it)
//        }
//    }

    fun onDoubleClick(eh : (ITEM_TYPE) -> Unit) {
        this.onDoubleClick = eh
    }

    fun toBackend() : TableViewBackend<ITEM_TYPE> {
        TableViewBackend<ITEM_TYPE>().also { table ->
            table.cells += cells.map { cell -> cell.toTableCellDef(table) }
            // FIXME table items to mutable list, might be bad performance-wise to to it by default
            table.allItems = items.map { TableItem(it) }.toMutableList()
            table.viewportItems = table.allItems
            table.gap = gap
            table.tableTheme = theme
            table.onDoubleClick = onDoubleClick
            return table
        }
    }

    companion object {
        fun <ITEM_TYPE> tableBackend(buildFun : TableViewBackendBuilder<ITEM_TYPE>.() -> Unit) : TableViewBackend<ITEM_TYPE> {
            return TableViewBackendBuilder<ITEM_TYPE>().apply(buildFun).toBackend()
        }
    }

}
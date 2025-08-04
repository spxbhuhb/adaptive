package `fun`.adaptive.ui.table

import `fun`.adaptive.ui.table.renderer.tableItemToString

class TableViewBackendBuilder<ITEM_TYPE> {

    val cells = mutableListOf<TableCellDefBuilder<ITEM_TYPE, *>>()

    fun intCell(buildFun: TableCellDefBuilder<ITEM_TYPE, Int>.() -> Unit) {
        cells += TableCellDefBuilder<ITEM_TYPE, Int>().also {
            //it.content = ::tableItemToString
            buildFun(it)
        }
    }

}
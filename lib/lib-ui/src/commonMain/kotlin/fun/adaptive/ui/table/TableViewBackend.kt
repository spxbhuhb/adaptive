package `fun`.adaptive.ui.table

import `fun`.adaptive.general.SelfObservable

class TableViewBackend : SelfObservable<TableViewBackend>() {

    val cells : MutableList<TableCellDef> = mutableListOf()
    val tableTheme = TableTheme.default

}
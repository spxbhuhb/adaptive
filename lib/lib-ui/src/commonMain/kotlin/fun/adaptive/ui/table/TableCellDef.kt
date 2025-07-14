package `fun`.adaptive.ui.table

import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.menu.MenuItemBase
import kotlin.properties.Delegates.observable

class TableCellDef(
    val table: TableViewBackend,
    val name: String,
    val size: DPixel,
    val visible: Boolean = true,
    val sortable: Boolean = true
) : SelfObservable<TableCellDef>() {

    var sorting by observable(Sorting.None, ::notify)
    var rowMenu by observable(emptyList<MenuItemBase<Any>>(), ::notify)

}
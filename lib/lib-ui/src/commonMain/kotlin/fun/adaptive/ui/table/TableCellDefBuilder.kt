package `fun`.adaptive.ui.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.layout.GridTrack
import `fun`.adaptive.ui.menu.MenuItemBase

class TableCellDefBuilder<ITEM, CELL_DATA> {

    var label = ""
    var width : GridTrack = 1.fr
    var minWidth = 100.dp

    var visible = true
    var sortable = true
    var resizable = true

    var decimals : Int = 2

    var rowMenu = emptyList<MenuItemBase<Any>>()

    var instructions = emptyInstructions

    var get: ((ITEM) -> CELL_DATA)? = null

    var content: @Adaptive ((TableCellDef<ITEM, CELL_DATA>, ITEM) -> Any)? = null

    fun toTableCellDef(tableBackend : TableViewBackend<ITEM>) : TableCellDef<ITEM, CELL_DATA> {
        TableCellDef(
           tableBackend, label, width, minWidth,
            if (instructions === emptyInstructions) {
                tableBackend.tableTheme.cellContainer
            } else {
                instructions
            },
            get!!, content!!
        ).also { cell ->
            cell.visible = visible
            cell.sortable = sortable
            cell.decimals = decimals
            cell.rowMenu = rowMenu
            cell.resizable = resizable

            return cell
        }
    }
}
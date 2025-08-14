package `fun`.adaptive.ui.table

import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.layout.GridTrack
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.utility.UUID

class TableCellDefBuilder<ITEM, CELL_DATA> {

    var label = ""
    var width : GridTrack = 1.fr
    var minWidth = 100.dp

    var visible = true
    var sortable = true
    var resizable = true
    var supportsTextFilter = true

    var decimals : Int = 2
    var unit : String? = null

    var rowMenu = emptyList<MenuItemBase<Any>>()

    var headerInstructions : (() -> AdaptiveInstructionGroup)? = null
    var instructions : ((ITEM) -> AdaptiveInstructionGroup)? = null

    var get : ((ITEM) -> CELL_DATA)? = null
    var matchFun : ((CELL_DATA, filterText : String) -> Boolean)? = null

    var content : @Adaptive ((TableCellDef<ITEM, CELL_DATA>, ITEM) -> Any)? = null

    var roleUuid : UUID<*>? = null

    fun toTableCellDef(tableBackend : TableViewBackend<ITEM>) : TableCellDef<ITEM, CELL_DATA> {
        TableCellDef(
            tableBackend, label, width, minWidth,
            instructions ?: { tableBackend.tableTheme.cellContainer },
            get !!, matchFun, content !!
        ).also { cell ->
            cell.visible = visible
            cell.sortable = sortable
            cell.decimals = decimals
            cell.unit = unit
            cell.rowMenu = rowMenu
            cell.resizable = resizable
            cell.roleUuid = roleUuid
            cell.supportsTextFilter = supportsTextFilter
            cell.headerInstructions = headerInstructions
            return cell
        }
    }
}
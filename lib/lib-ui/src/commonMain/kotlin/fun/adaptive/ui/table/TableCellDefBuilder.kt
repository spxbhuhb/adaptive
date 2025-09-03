package `fun`.adaptive.ui.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.layout.GridTrack
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.utility.UUID

class TableCellDefBuilder<ITEM, CELL_DATA> {

    var key : String? = null

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

    var role : UUID<*>? = null
    var group : TableCellGroupDef? = null

    fun toTableCellDef(tableBackend : TableViewBackend<ITEM>) : TableCellDef<ITEM, CELL_DATA> {
        TableCellDef(
            tableBackend,
            label,
            width,
            minWidth,
            instructions ?: { tableBackend.tableTheme.cellContainer },
            get !!,
            matchFun,
            content !!,
            key = key,
            visible = visible,
            sortable = sortable,
            decimals = decimals,
            unit = unit,
            rowMenu = rowMenu,
            resizable = resizable,
            role = role,
            supportsTextFilter = supportsTextFilter,
            headerInstructions = headerInstructions,
            group = group
        ).also { cell ->
            return cell
        }
    }
}
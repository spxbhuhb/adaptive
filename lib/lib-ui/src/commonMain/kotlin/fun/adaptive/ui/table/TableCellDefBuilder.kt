package `fun`.adaptive.ui.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.menu.MenuItemBase
import kotlin.reflect.KFunction2

class TableCellDefBuilder<ITEM, CELL_DATA> {

    var label = ""
    var width = 1.fr

    var visible = true
    var sortable = true

    var rowMenu = emptyList<MenuItemBase<Any>>()

    var instructions = emptyInstructions

    var get: ((ITEM) -> CELL_DATA)? = null

    //var content: @Adaptive ((TableCellDef<ITEM, CELL_DATA>, ITEM) -> Any)? = null
}

typealias AFunction2<P0, P1> = KFunction2<P0, P1, Any>


val a: Function2<String, Int, Int>? = ::f

val b: KFunction2<String, Int, Int>? = ::f

val c: AFunction2<String, Int>? = ::f

fun <T> f(s: String, v: T): T {
    return v
}
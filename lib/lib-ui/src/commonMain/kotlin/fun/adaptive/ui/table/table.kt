package `fun`.adaptive.ui.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.verticalScroll

@Adaptive
fun <ITEM> table(
    backend: TableViewBackend<ITEM>
): AdaptiveFragment {

    val observed = observe { backend }

//    row {
//        for (cell in observed.cells) {
//            tableHeaderCell(cell)
//        }
//    }

        column {
            verticalScroll
            for (item in backend.viewportItems ?: emptyList()) {
                tableItem(backend, item)
            }
        }

    return fragment()
}

@Adaptive
fun <ITEM> tableItem(
    backend: TableViewBackend<ITEM>,
    item : TableItem<ITEM>
) : AdaptiveFragment {

    for (cell in backend.cells) {
        tableCell(cell, item)
    }

    return fragment()
}

@Adaptive
fun <ITEM, CELL_DATA> tableCell(
    cellDef: TableCellDef<ITEM, CELL_DATA>,
    item: TableItem<ITEM>
) : AdaptiveFragment {
   // cellDef.renderFun(cellDef, item.data)
    return fragment()
}
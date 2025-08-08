package `fun`.adaptive.doc.example.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.FragmentTraceContext
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.generated.resources.north
import `fun`.adaptive.ui.generated.resources.south
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.table.TableItem
import `fun`.adaptive.ui.table.table

/**
 * # Basic Table
 */
@Adaptive
fun tableBasicExample(): AdaptiveFragment {

//    val backend = tableViewBackend<TableItemData> {
//
//        cell(
//            label = "Cell 1",
//            width = 200.dp,
//            getFun = { it.int1 },
//            menu = listOf(
//                MenuItem(Graphics.south, "Sort descending", "Sort descending"),
//                MenuItem(Graphics.north, "Sort ascending", "Sort ascending")
//            )
//        )
//
//        cell(
//            "Cell 2",
//            200.dp,
//            getFun = { it.int2 }
//        )
//
//        viewportItems = mutableListOf(
//            TableItem(TableItemData()),
//        )
//
//    }
//
//    adapter().traceWithContext = true
//    localContext(FragmentTraceContext()) {
//        table(backend)
//    }

    return fragment()
}

class TableItemData(
    val int1 : Int = 12,
    val int2 : Int = 34
)
package `fun`.adaptive.doc.example.table

import `fun`.adaptive.doc.example.generated.resources.edit
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.FragmentTraceContext
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.generated.resources.account_circle
import `fun`.adaptive.ui.generated.resources.close
import `fun`.adaptive.ui.generated.resources.north
import `fun`.adaptive.ui.generated.resources.south
import `fun`.adaptive.ui.icon.ActionIconRowBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.snackbar.successNotification
import `fun`.adaptive.ui.table.TableItem
import `fun`.adaptive.ui.table.TableViewBackendBuilder.Companion.tableBackend
import `fun`.adaptive.ui.table.table

/**
 * # Basic Table
 *
 * Define a backend with [tableBackend](function://) and pass it to the [table](fragment://) fragment.
 *
 * The [table](fragment://) uses [cellBox](fragment://) to render the items in the table.
 * We don't use the term row because [cellBox](fragment://) is able to rearrange itself
 * depending on the available space, and when there is not enough space, items may be stacked vertically.
 */
@Adaptive
fun tableBasicExample(): AdaptiveFragment {

    val backend = tableBackend {

        items = listOf(
            TableItemData1(12, 23),
            TableItemData1(34, 45)
        )

        intCell {
            label = "Cell 1"
            minWidth = 100.dp
            get = { it.int1 }
        }

        intCell {
            label = "Cell 1"
            minWidth = 100.dp
            get = { it.int2 }
        }

        actionsCell {
            label = "Actions"
            minWidth = 100.dp
            get = {
                ActionIconRowBackend(
                    priorityActions = listOf(
                        MenuItem(Graphics.edit, label = "Edit", data = it),
                    ),
                    otherActions = listOf(
                        MenuItem(Graphics.north, label = "Move up", data = it),
                        MenuItem(Graphics.south, label = "Move down", data = it),
                    )
                ) { item ->
                    successNotification("Selected: ${item.label} for ${item.data}")
                }
            }
        }

    }

    table(backend) .. height { 200.dp }

    return fragment()
}

private data class TableItemData1(
    val int1 : Int,
    val int2 : Int
)
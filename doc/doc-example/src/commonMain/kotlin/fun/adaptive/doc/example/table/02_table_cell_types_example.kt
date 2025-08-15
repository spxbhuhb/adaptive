package `fun`.adaptive.doc.example.table

import `fun`.adaptive.doc.example.generated.resources.edit
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.generated.resources.account_circle
import `fun`.adaptive.ui.generated.resources.north
import `fun`.adaptive.ui.generated.resources.south
import `fun`.adaptive.ui.icon.ActionIconRowBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.snackbar.successNotification
import `fun`.adaptive.ui.table.TableViewBackendBuilder.Companion.tableBackend
import `fun`.adaptive.ui.table.table
import kotlin.time.Clock

/**
 * # Table — Cell types
 *
 * Demonstrates the built-in cell builders:
 * - iconCell, stringCell, doubleCell (with decimals/unit), statusCell, timeAgoCell, actionsCell
 */
@Adaptive
fun tableCellTypesExample(): AdaptiveFragment {

    val now = Clock.System.now()

    val backend = tableBackend<TableRow2> {
        items = listOf(
            TableRow2(id = "A-1", name = "Device A", value = 12.345, statuses = setOf("online"), lastChange = now),
            TableRow2(id = "B-2", name = "Device B", value = 0.5, statuses = setOf("offline", "warn"), lastChange = now)
        )

        iconCell {
            label = "Icon"
            minWidth = 32.dp
            width = 32.dp
            get = { Graphics.account_circle }
        }

        stringCell {
            label = "# ID"
            minWidth = 120.dp
            width = 120.dp
            get = { it.id }
        }

        stringCell {
            label = "Name"
            minWidth = 200.dp
            get = { it.name }
        }

        doubleCell {
            label = "Value"
            minWidth = 120.dp
            decimals = 2
            unit = "A"
            get = { it.value }
        }

        statusCell {
            label = "Status"
            minWidth = 120.dp
            get = { it.statuses }
        }

        timeAgoCell {
            label = "Last change"
            minWidth = 140.dp
            get = { it.lastChange }
        }

        actionsCell {
            label = "Actions"
            minWidth = 100.dp
            get = { row ->
                ActionIconRowBackend(
                    priorityActions = listOf(
                        MenuItem(Graphics.edit, label = "Edit", data = row),
                    ),
                    otherActions = listOf(
                        MenuItem(Graphics.north, label = "Move up", data = row),
                        MenuItem(Graphics.south, label = "Move down", data = row),
                    )
                ) { item ->
                    successNotification("Selected: ${item.label} for ${item.data}")
                }
            }
        }
    }

    table(backend) .. height { 260.dp }

    return fragment()
}

data class TableRow2(
    val id: String?,
    val name: String?,
    val value: Double?,
    val statuses: Set<String>?,
    val lastChange: kotlin.time.Instant?
)

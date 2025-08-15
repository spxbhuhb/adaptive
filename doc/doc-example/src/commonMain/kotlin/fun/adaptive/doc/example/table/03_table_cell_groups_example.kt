package `fun`.adaptive.doc.example.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.table.TableViewBackendBuilder.Companion.tableBackend
import `fun`.adaptive.ui.table.table

/**
 * # Table — Cell groups and responsiveness
 *
 * Shows how to group related cells with `cellGroup(label, priority)` to help the table
 * rearrange content when space is limited. Lower "priority" values are grouped earlier.
 */
@Adaptive
fun tableCellGroupsExample(): AdaptiveFragment {

    val backend = tableBackend<GRow> {
        items = listOf(
            GRow("R-01", 10.0, 11.0, 12.0),
            GRow("R-02", 20.0, 21.0, 22.0),
        )

        stringCell {
            label = "# ID"
            minWidth = 120.dp
            width = 120.dp
            get = { it.id }
        }

        val currents = cellGroup("Currents", priority = 1)

        doubleCell {
            label = "I L1"
            minWidth = 100.dp
            get = { it.l1 }
            group = currents
        }
        doubleCell {
            label = "I L2"
            minWidth = 100.dp
            get = { it.l2 }
            group = currents
        }
        doubleCell {
            label = "I L3"
            minWidth = 100.dp
            get = { it.l3 }
            group = currents
        }
    }

    // When width is insufficient, the "Currents" cells collapse vertically as a group.
    table(backend) .. height { 220.dp }

    return fragment()
}

data class GRow(
    val id: String?,
    val l1: Double?,
    val l2: Double?,
    val l3: Double?
)

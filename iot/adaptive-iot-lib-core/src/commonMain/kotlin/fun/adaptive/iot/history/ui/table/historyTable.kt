package `fun`.adaptive.iot.history.ui.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.history.ui.HistoryContentController
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun historyTable(
    controller: HistoryContentController,
) {
    val context = valueFrom { controller.chartContext }

    column {
        gap { 4.dp }

        when (context.items.size) {

            1 -> {
                singleHistoryTableHeader(controller)
                singleHistoryTableContent(controller)
            }

            else -> {
                multiHistoryTableHeader(controller)
                multiHistoryTableContent(controller)
            }
        }
    }
}
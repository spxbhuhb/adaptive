package `fun`.adaptive.iot.history.ui.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.history.ui.HistoryBrowserWsItem
import `fun`.adaptive.iot.history.ui.HistoryContentController
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun historyTable(
    item : HistoryBrowserWsItem,
    controller: HistoryContentController,
) {
    val context = valueFrom { controller.chartContext }

    column {
        gap { 4.dp } .. fill.constrain .. padding { 16.dp }

        when (context.items.size) {

            1 -> {
                singleHistoryTableHeader(controller)
                column {
                    maxSize .. gap { 4.dp } .. verticalScroll
                    singleHistoryTableContent(controller)
                }
            }

            else -> {
                multiHistoryTableHeader(item, controller)
                column {
                    maxSize .. gap { 4.dp } .. verticalScroll
                    multiHistoryTableContent(controller)
                }
            }
        }
    }
}
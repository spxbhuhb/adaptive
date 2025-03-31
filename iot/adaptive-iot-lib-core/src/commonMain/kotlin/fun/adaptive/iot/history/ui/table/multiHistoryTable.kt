package `fun`.adaptive.iot.history.ui.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.common.timestamp
import `fun`.adaptive.iot.generated.resources.timestamp
import `fun`.adaptive.iot.history.ui.HistoryBrowserWsItem
import `fun`.adaptive.iot.history.ui.HistoryContentController
import `fun`.adaptive.iot.history.ui.historyName
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.normalFont
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.textMedium
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.utility.format

@Adaptive
fun multiHistoryTableHeader(
    hisItem: HistoryBrowserWsItem,
    controller: HistoryContentController
) {
    val context = valueFrom { controller.chartContext }

    grid {
        controller.theme.historyListHeader .. colTemplate(160.dp, 1.fr repeat context.items.size)

        text(Strings.timestamp) .. textMedium .. normalFont
        for (item in context.items) {
            text(historyName(hisItem.controller, controller, item)) .. textMedium .. normalFont .. maxWidth
        }
    }
}

@Adaptive
fun multiHistoryTableContent(
    controller: HistoryContentController
) {
    val context = valueFrom { controller.chartContext }

    val columns = controller.multiTableColumns(context)
    val markerColumn = columns.first
    val valueColumns = columns.second

    val template = colTemplate(160.dp, 1.fr repeat context.items.size)

    for (i in 0 until markerColumn.size) {
        grid {
            controller.theme.historyRecordContainer .. template

            timestamp(markerColumn[i]) .. textSmall

            for (valueColumn in valueColumns) {
                text(valueColumn.cells[i]?.value?.format(1) ?: "")
            }
        }
    }
}
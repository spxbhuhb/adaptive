package `fun`.adaptive.iot.history.ui.table

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.common.alarmIcon
import `fun`.adaptive.iot.common.status
import `fun`.adaptive.iot.common.timestamp
import `fun`.adaptive.iot.generated.resources.alarm
import `fun`.adaptive.iot.generated.resources.status
import `fun`.adaptive.iot.generated.resources.timestamp
import `fun`.adaptive.iot.generated.resources.value
import `fun`.adaptive.iot.history.ui.HistoryContentController
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.textMedium
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.utility.format
import `fun`.adaptive.value.item.AvStatus

@Adaptive
fun singleHistoryTableHeader(
    controller: HistoryContentController,
) {
    grid {
        controller.theme.historyListHeader .. controller.theme.singleHistoryColumns

        text(Strings.timestamp) .. textMedium .. normalFont
        text(Strings.value) .. textMedium .. normalFont .. paddingLeft { 24.dp }
        text(Strings.alarm) .. textMedium .. normalFont
        text(Strings.status) .. alignSelf.center .. textMedium .. normalFont
    }
}

@Adaptive
fun singleHistoryTableContent(
    controller: HistoryContentController
) {
    val context = valueFrom { controller.chartContext }

    for (record in context.items.first().sourceData) {
        grid {
            controller.theme.historyRecordContainer .. controller.theme.singleHistoryColumns

            timestamp(record.y.timestamp) .. textSmall
            text(record.y.value.format(1)) .. paddingLeft { 24.dp }
            alarmIcon(record.y.flags)
            status(AvStatus(record.y.flags))
        }
    }
}
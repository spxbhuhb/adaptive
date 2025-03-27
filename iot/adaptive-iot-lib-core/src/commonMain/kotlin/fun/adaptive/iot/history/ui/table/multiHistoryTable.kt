package `fun`.adaptive.iot.history.ui.table

import `fun`.adaptive.chart.calculation.CalculationContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.common.timestamp
import `fun`.adaptive.iot.generated.resources.timestamp
import `fun`.adaptive.iot.history.model.AioDoubleHistoryRecord
import `fun`.adaptive.iot.history.ui.HistoryContentController
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.normalFont
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.textMedium
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.utility.format
import `fun`.adaptive.value.item.AvItem
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes

@Adaptive
fun multiHistoryTableHeader(
    controller: HistoryContentController
) {
    val context = valueFrom { controller.chartContext }

    grid {
        controller.theme.historyListHeader .. colTemplate(160.dp, 1.fr repeat context.items.size)

        text(Strings.timestamp) .. textMedium .. normalFont
        for (item in context.items) {
            text(item.attachment?.name) .. textMedium .. normalFont
        }
    }
}

@Adaptive
fun multiHistoryTableContent(
    controller: HistoryContentController
) {
    val context = valueFrom { controller.chartContext }

    val iStart = context.range?.xStart ?: now()
    val iEnd = context.range?.xEnd ?: now()

    val cc = CalculationContext<Instant, AioDoubleHistoryRecord, AvItem<*>>(
        iStart,
        iEnd,
        context.normalizer.normalizedInterval(now(), now() + 15.minutes),
        context.normalizer
    ) { chartItem, start, end ->
        chartItem.sourceData[start].y
    }

    val markerColumn = cc.markers
    val valueColumns = context.items.map { it.normalize(cc.normalizer).prepareCells(cc) }

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
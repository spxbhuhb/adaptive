package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.chart.model.ChartItem
import `fun`.adaptive.iot.common.localizedString
import `fun`.adaptive.iot.generated.resources.timestamp
import `fun`.adaptive.iot.history.model.AioDoubleHistoryRecord
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.platform.download.downloadFile
import `fun`.adaptive.utility.format
import `fun`.adaptive.value.item.AvItem
import kotlinx.datetime.Instant

fun downloadReport(
    item : HistoryBrowserWsItem,
    controller: HistoryContentController
) {

    val columns = controller.multiTableColumns(controller.chartContext.value)
    val markerColumn = columns.first
    val valueColumns = columns.second

    val out = StringBuilder()

    out.append(Strings.timestamp)
    out.append(";")
    for (valueColumn in valueColumns) {
        out.append(columnName(item.controller, valueColumn))
        out.append(";;")
    }
    out.appendLine()

    for (i in 0 until markerColumn.size) {
        out.append(markerColumn[i]?.localizedString() ?: "")
        out.append(";")

        for (valueColumn in valueColumns) {
            val cell = valueColumn.cells[i]

            out.append(cell?.value?.format(1) ?: "")
            out.append(";")

            out.append(cell?.flags ?: "")
            out.append(";")
        }

        out.appendLine()
    }

    downloadFile(out.toString().encodeToByteArray(), "history.csv", "text/csv")
}

fun columnName(
    controller : HistoryToolController,
    valueColumn : ChartItem<Instant, AioDoubleHistoryRecord, AvItem<*>>
): String {
    val itemName = valueColumn.attachment?.name ?: ""
    val names = historyPathNames(controller, valueColumn.attachment!!)

    return if (names.isEmpty()) {
        itemName
    } else {
        (listOf(itemName) + names.reversed()).joinToString(" / ")
    }
}
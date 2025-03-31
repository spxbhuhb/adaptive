package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.iot.common.localizedString
import `fun`.adaptive.iot.generated.resources.timestamp
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.platform.download.downloadFile
import `fun`.adaptive.utility.format

fun downloadReport(
    item: HistoryBrowserWsItem,
    controller: HistoryContentController
) {

    val columns = controller.multiTableColumns(controller.chartContext.value)
    val markerColumn = columns.first
    val valueColumns = columns.second

    val out = StringBuilder()

    out.append(Strings.timestamp)
    out.append(";")
    for (valueColumn in valueColumns) {
        out.append(historyName(item.controller, controller, valueColumn))
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

    val utf8Bom = byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte())

    downloadFile(utf8Bom + out.toString().encodeToByteArray(), "history.csv", "text/csv")
}
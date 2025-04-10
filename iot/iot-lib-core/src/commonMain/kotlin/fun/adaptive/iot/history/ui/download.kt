package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.iot.common.localizedString
import `fun`.adaptive.iot.generated.resources.timestamp
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.platform.download.downloadFile
import `fun`.adaptive.utility.format
import `fun`.adaptive.xlsx.conf.XlsxConfiguration
import `fun`.adaptive.xlsx.model.XlsxDocument
import `fun`.adaptive.xlsx.save
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun downloadCsv(
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
        out.append(historyName(item.controller, valueColumn))
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


fun downloadXlsx(
    item: HistoryBrowserWsItem,
    controller: HistoryContentController
) {
    val columns = controller.multiTableColumns(controller.chartContext.value)
    val markerColumn = columns.first
    val valueColumns = columns.second

    val cfg = XlsxConfiguration()

    val roundedNumberFormat = cfg.formats.newCustomNumberFormat("#,##0.0")

    val doc = XlsxDocument(cfg)

    val sheet = doc.newSheet("History")

    sheet.columns["A"].width = 18.5

    sheet[1,1].value = Strings.timestamp

    for (valueColumn in valueColumns.withIndex()) {
        sheet[2 + valueColumn.index * 2, 1].value = historyName(item.controller, valueColumn.value)
    }

    val timezone = TimeZone.currentSystemDefault()
    for (i in 0 until markerColumn.size) {
        markerColumn[i]?.let { sheet[1, 2 + i].value = it.toLocalDateTime(timezone) }
    }

    for ((valueColumnIndex, valueColumn) in valueColumns.withIndex()) {
        val column = 2 + valueColumnIndex * 2
        for (i in 0 until valueColumn.cells.size) {
            val row = 2 + i
            val valueCell = valueColumn.cells[i]

            valueCell?.let {
                val sheetCell = sheet[column, row]

                sheetCell.value = it.value
                sheetCell.numberFormat = roundedNumberFormat

                sheet[column + 1, row].value = it.flags
            }
        }
    }

    controller.scope.launch {
        doc.save("history.xlsx")
    }
}
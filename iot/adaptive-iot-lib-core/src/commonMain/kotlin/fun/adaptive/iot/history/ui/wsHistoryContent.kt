package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.common.AioTheme
import `fun`.adaptive.iot.common.alarmIcon
import `fun`.adaptive.iot.common.status
import `fun`.adaptive.iot.common.timestamp
import `fun`.adaptive.iot.generated.resources.*
import `fun`.adaptive.iot.history.ui.HistoryContentController.Mode
import `fun`.adaptive.iot.history.ui.chart.historyChart
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.filter.quickFilter
import `fun`.adaptive.ui.input.datetime.dateInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textMedium
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.utility.format
import `fun`.adaptive.utility.localDate
import `fun`.adaptive.value.item.AvStatus

@Adaptive
fun wsHistoryContent(
    pane: WsPane<HistoryBrowserWsItem, HistoryContentController>
): AdaptiveFragment {

    val items = pane.data.items
    val name = if (items.size == 1) items.first().name.ifEmpty { Strings.noname } else Strings.historicalData
    val mode = valueFrom { pane.controller.mode }

    column {
        maxSize .. padding { 16.dp } .. backgrounds.surface
        fill.constrain .. backgrounds.surfaceVariant

        title(pane.controller, name)

        if (mode == Mode.TABLE) {
            historyTableHeader()
            historyTable(pane.controller)
        } else {
            historyChart(pane.controller)
        }
    }

    return fragment()
}


@Adaptive
fun title(
    controller: HistoryContentController,
    name: String
) {
    var modeList = listOf(Mode.TABLE to Strings.table, Mode.CHART to Strings.chart)
    val mode = valueFrom { controller.mode }
    val currentMode = modeList.first { it.first == mode }

    var status2 = ""
    var start = localDate()
    var end = localDate()

    column {
        gap { 24.dp } .. paddingBottom { 16.dp } .. height { 144.dp }

        grid {
            maxWidth .. colTemplate(1.fr, 1.fr)
            paddingBottom { 32.dp } .. height { 56.dp }

            h2(name)

            button(Strings.downloadReport) .. alignSelf.end .. onClick {
                downloadReport(controller)
            }
        }

        row {
            gap { 16.dp }
            quickFilter(currentMode, modeList, { second }) { v -> controller.mode.value = v.first }
            quickFilter(status2, listOf("Ma", "Hét", "Hónap", "Egyedi"), { this }) { v -> status2 = v }
            dateInput(start) { v -> start = v }
            dateInput(end) { v -> end = v }
        }
    }
}

@Adaptive
fun historyTableHeader(
    theme: AioTheme = AioTheme.DEFAULT
) {
    grid {
        theme.historyListHeader

        text(Strings.timestamp) .. textMedium .. normalFont
        text(Strings.value) .. textMedium .. normalFont .. paddingLeft { 24.dp }
        text(Strings.alarm) .. textMedium .. normalFont
        text(Strings.status) .. alignSelf.center .. textMedium .. normalFont
    }
}

@Adaptive
fun historyTable(
    controller: HistoryContentController,
    theme: AioTheme = AioTheme.DEFAULT
) {
    val contentItems = valueFrom { controller.chartItems }

    column {
        gap { 4.dp }

        when (contentItems.size) {
            0 -> box { }
            1 -> singleHistoryTable(controller, theme)
            else -> multiHistoryTable(controller, theme)
        }
    }
}

@Adaptive
fun singleHistoryTable(
    controller: HistoryContentController,
    theme: AioTheme
) {
    val contentItems = valueFrom { controller.chartItems }

    for (record in contentItems.first().sourceData) {
        grid {
            theme.historyRecordContainer

            timestamp(record.y.timestamp) .. textSmall
            text(record.y.value.format(1)) .. paddingLeft { 24.dp }
            alarmIcon(record.y.flags)
            status(AvStatus(record.y.flags))
        }
    }
}

@Adaptive
fun multiHistoryTable(
    controller: HistoryContentController,
    theme: AioTheme
) {

}

fun downloadReport(controller: HistoryContentController) {


//    val out = StringBuilder()
//    for (contentItem in contentItems) {
//        for (record in contentItem.records) {
//            out.append("${contentItem.point.name};${record.timestamp.localizedString()};${record.value};${record.flags}\n")
//        }
//    }
//
//    downloadFile(out.toString().encodeToByteArray(), "history.csv", "text/csv")
}
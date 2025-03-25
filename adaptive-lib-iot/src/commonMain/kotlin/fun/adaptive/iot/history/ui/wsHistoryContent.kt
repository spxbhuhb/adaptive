package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.common.*
import `fun`.adaptive.iot.history.model.AioDoubleHistoryRecord
import `fun`.adaptive.iot.history.ui.HistoryContentController.Mode
import `fun`.adaptive.iot.history.ui.chart.historyChart
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.filter.quickFilter
import `fun`.adaptive.ui.input.datetime.dateInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.ui.platform.download.downloadFile
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textMedium
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.utility.format
import `fun`.adaptive.utility.localDate
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem
import `fun`.adaptive.value.item.AvStatus

@Adaptive
fun wsHistoryContent(
    pane: WsPane<HistoryBrowserWsItem, HistoryContentController>
): AdaptiveFragment {

    val originalItem = copyOf { pane.data.item }.asAvItem<AioPointSpec>()
    val records = fetch { pane.controller.query(originalItem) } ?: emptyList()
    val mode = valueFrom { pane.controller.mode }

    column {
        maxSize .. padding { 16.dp } .. backgrounds.surface
        fill.constrain .. backgrounds.surfaceVariant

        title(pane.controller, originalItem, records)

        if (mode == Mode.TABLE) {
            historyTableHeader()
            historyTable(records)
        } else {
            historyChart(pane.controller, records)
        }
    }

    return fragment()
}


@Adaptive
fun title(
    controller : HistoryContentController,
    item: AvItem<AioPointSpec>,
    records: List<AioDoubleHistoryRecord>
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

            column {
                h2(item.name.ifEmpty { Strings.noname })
                uuidLabel { item.uuid }
            }

            button(Strings.downloadReport) .. alignSelf.end .. onClick {
                downloadReport(records)
            }
        }

        row {
            gap { 16.dp }
            quickFilter(currentMode, modeList, { second}) { v -> controller.mode.value = v.first }
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
    records: List<AioDoubleHistoryRecord>,
    theme: AioTheme = AioTheme.DEFAULT
) {
    column {
        gap { 4.dp }
        for (record in records) {
            grid {
                theme.historyRecordContainer

                timestamp(record.timestamp) .. textSmall
                text(record.value.format(1)) .. paddingLeft { 24.dp }
                alarmIcon(record.flags)
                status(AvStatus(record.flags))
            }
        }
    }
}

fun downloadReport(records: List<AioDoubleHistoryRecord>) {
    val out = StringBuilder()
    for (record in records) {
        out.append("${record.timestamp.localizedString()};${record.value};${record.flags}\n")
    }

    downloadFile(out.toString().encodeToByteArray(), "history.csv", "text/csv")
}
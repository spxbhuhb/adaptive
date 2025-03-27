package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.generated.resources.*
import `fun`.adaptive.iot.history.ui.HistoryContentController.Mode
import `fun`.adaptive.iot.history.ui.chart.historyChart
import `fun`.adaptive.iot.history.ui.table.historyTable
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.filter.quickFilter
import `fun`.adaptive.ui.input.datetime.dateInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.platform.download.downloadFile
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.utility.localDate

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

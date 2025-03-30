package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.generated.resources.downloadReport
import `fun`.adaptive.iot.generated.resources.historicalData
import `fun`.adaptive.iot.generated.resources.noname
import `fun`.adaptive.iot.history.ui.HistoryContentController.Mode
import `fun`.adaptive.iot.history.ui.chart.historyChart
import `fun`.adaptive.iot.history.ui.settings.historySettings
import `fun`.adaptive.iot.history.ui.table.historyTable
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.arrow_right
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.icon.tableIconTheme
import `fun`.adaptive.ui.input.datetime.dateInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textColors
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

        title(pane, name)

        when (mode.selected) {
            Mode.TABLE -> historyTable(pane.data, pane.controller)
            Mode.CHART -> historyChart(pane.controller)
            Mode.SETTINGS -> historySettings(pane.controller)
        }
    }

    return fragment()
}


@Adaptive
fun title(
    pane: WsPane<HistoryBrowserWsItem, HistoryContentController>,
    name: String
) {
    val controller = pane.controller
    val mode = valueFrom { controller.mode }

    var status2 = ""
    var start = localDate()
    var end = localDate()

    column {
        paddingBottom { 40.dp }

        grid {
            maxWidth .. colTemplate(1.fr, 1.fr) .. height { 32.dp }

            column {
                h2(name)
                historyPath(pane.data)
            }

            row {
                alignSelf.endCenter .. alignItems.center .. gap { 24.dp } .. backgrounds.surfaceVariant

                row {
                    modeIcon(controller, Mode.SETTINGS)
                    modeIcon(controller, Mode.CHART)
                    modeIcon(controller, Mode.TABLE)
                }

                row {
                    gap { 8.dp } .. paddingRight { 8.dp }
                    dateInput(start) { v -> start = v } .. width { 112.dp } .. alignItems.center
                    dateInput(end) { v -> end = v } .. width { 112.dp } .. alignItems.center
                }

                button(Strings.downloadReport) .. onClick {
                    downloadReport(pane.data, controller)
                }
            }
        }
    }
}

@Adaptive
fun modeIcon(controller: HistoryContentController, mode : Mode) {
    actionIcon(mode.icon, tooltip = mode.label, theme = tableIconTheme) .. onClick { controller.switchMode(mode) }
}

@Adaptive
fun historyPath(item: HistoryBrowserWsItem) {
    val names = historyPathNames(item.controller, item.items.first())

    row {
        alignItems.center

        for (name in names.indices) {
            text(names[name]) .. textColors.onSurfaceVariant
            if (name < names.size - 1) {
                icon(Graphics.arrow_right) .. textColors.onSurfaceVariant
            }
        }
    }

}
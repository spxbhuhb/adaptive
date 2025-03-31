package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.generated.resources.downloadReport
import `fun`.adaptive.iot.generated.resources.historicalData
import `fun`.adaptive.iot.generated.resources.noname
import `fun`.adaptive.iot.generated.resources.settings
import `fun`.adaptive.iot.history.ui.HistoryContentController.Mode
import `fun`.adaptive.iot.history.ui.chart.historyChart
import `fun`.adaptive.iot.history.ui.settings.historySettings
import `fun`.adaptive.iot.history.ui.table.historyTable
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.arrow_right
import `fun`.adaptive.ui.builtin.settings
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.filter.QuickFilterModel
import `fun`.adaptive.ui.fragment.layout.SplitPaneConfiguration
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.icon.tableIconTheme
import `fun`.adaptive.ui.input.datetime.dateInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.splitpane.splitPaneDivider
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textMedium
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.utility.localDate

@Adaptive
fun wsHistoryContent(
    pane: WsPane<HistoryBrowserWsItem, HistoryContentController>
): AdaptiveFragment {

    val items = pane.data.items
    val name = if (items.size == 1) items.first().name.ifEmpty { Strings.noname } else Strings.historicalData
    val mode = valueFrom { pane.controller.mode }
    val splitConfig = valueFrom { pane.controller.splitConfig }

    column {
        maxSize .. padding { 16.dp } .. backgrounds.surface .. fill.constrain

        title(pane, name, splitConfig)

        splitPane(
            splitConfig,
            { legendContent(pane) },
            { splitPaneDivider() },
            { modeContent(mode, pane) }
        ) .. maxSize .. border(colors.lightOutline) .. cornerRadius { 4.dp }
    }

    return fragment()
}

@Adaptive
fun title(
    pane: WsPane<HistoryBrowserWsItem, HistoryContentController>,
    name: String,
    splitConfig: SplitPaneConfiguration
) {
    val controller = pane.controller

    column {
        paddingBottom { 8.dp }

        grid {
            maxWidth .. colTemplate(1.fr, 1.fr) .. height { 72.dp }

            column {
                h2(name)
                row {
                    actionIcon(Graphics.settings, tooltip = Strings.settings, theme = tableIconTheme) .. onClick {
                        controller.splitConfig.value = splitConfig.toggleFirst()
                    }
                    modeIcon(controller, Mode.CHART)
                    modeIcon(controller, Mode.TABLE)
                }
            }

            row {
                maxWidth .. alignItems.end .. gap { 24.dp }
                button(Strings.downloadReport) .. onClick {
                    downloadReport(pane.data, controller)
                }
            }
        }
    }
}

@Adaptive
private fun legendContent(pane: WsPane<HistoryBrowserWsItem, HistoryContentController>) {

    val context = valueFrom { pane.controller.chartContext }

    column {
        maxSize .. verticalScroll .. padding { 16.dp } .. gap { 16.dp }

        historySettings(pane.controller)

        column {
            gap { 8.dp }
            for (item in context.items) {
                row {
                    gap { 8.dp }
                    box { size(20.dp) .. backgroundColor(historyColor(item)) .. cornerRadius { 4.dp } }
                    text(historyName(pane, item)) .. textMedium
                }
            }
        }
    }
}

@Adaptive
private fun modeContent(mode: QuickFilterModel<Mode>, pane: WsPane<HistoryBrowserWsItem, HistoryContentController>) {
    when (mode.selected) {
        Mode.TABLE -> historyTable(pane.data, pane.controller)
        Mode.CHART -> historyChart(pane.controller)
    }
}

@Adaptive
fun modeIcon(controller: HistoryContentController, mode: Mode) {
    actionIcon(mode.icon, tooltip = mode.label, theme = tableIconTheme) .. onClick { controller.switchMode(mode) }
}
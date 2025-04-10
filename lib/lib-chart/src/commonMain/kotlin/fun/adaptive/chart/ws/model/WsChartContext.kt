package `fun`.adaptive.chart.ws.model

import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsContext

/**
 * This context holds data shared between chart content panes.
 *
 * The concept behind this is that charts may handle a large amount of data,
 * and you typically do not want to load them again if they are already loaded.
 *
 * Therefore, this context is a common store for all series handled by the given
 * workspace.
 */
class WsChartContext(
    override val workspace: Workspace
) : WsContext {

    val items = mutableListOf<WsItemChartSeries>()

//    val data = mutableMapOf<UUID<WsItemChartSeries>, ChartRenderSeries>()

    fun openChart(item: WsItemChartSeries, modifiers: Set<EventModifier>) {
        workspace.addContent(item, modifiers)
    }

    companion object {
        const val CHART_TOOL_PANE_KEY = "chart:builder"
        const val CHART_CONTENT_PANE_KEY = "chart:view"

        const val BASIC_HORIZONTAL_AXIS = "chart:axis:horizontal:basic"
        const val BASIC_VERTICAL_AXIS = "chart:axis:vertical:basic"

        const val BASIC_LINE_SERIES = "chart:series:line:basic"

        const val WSIT_CHART_SERIES = "chart:series"
    }

}
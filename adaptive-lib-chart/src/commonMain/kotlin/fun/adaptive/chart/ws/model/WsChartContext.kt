package `fun`.adaptive.chart.ws.model

import `fun`.adaptive.chart.model.ChartSeries
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsContext
import `fun`.adaptive.utility.UUID

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

    val data = mutableMapOf<UUID<WsItemChartSeries>, ChartSeries>()

    fun openChart(item: WsItemChartSeries) {
        workspace.addContent(item)
    }

    companion object {
        const val CHART_TOOL_PANE_KEY = "chart:builder"
        const val CHART_CONTENT_PANE_KEY = "chart:view"

        const val CHART_SERIES_ITEM_TYPE = "chart:series"
    }

}
package `fun`.adaptive.chart

import `fun`.adaptive.chart.ws.logic.WsChartContentController
import `fun`.adaptive.chart.ws.model.WsChartContext
import `fun`.adaptive.chart.ws.model.WsChartPaneData
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.builtin.menu
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.utility.UUID

fun chartCommon() {

}

fun AdaptiveAdapter.chartCommon() {
    fragmentFactory += arrayOf(ChartFragmentFactory)
}

fun Workspace.chartCommon() {
    contexts += WsChartContext(this)

    toolPanes += WsPane(
        UUID(),
        "Chart",
        Graphics.menu,
        WsPanePosition.RightTop,
        WsChartContext.CHART_TOOL_PANE_KEY,
        model = Unit
    )

    addContentPaneBuilder(WsChartContext.CHART_SERIES_ITEM_TYPE) { item ->

        WsPane(
            UUID(),
            item.name,
            item.icon,
            WsPanePosition.Center,
            WsChartContext.CHART_CONTENT_PANE_KEY,
            model = WsChartPaneData(),
            controller = WsChartContentController()
        )

    }

}
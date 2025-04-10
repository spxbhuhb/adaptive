package `fun`.adaptive.chart

import `fun`.adaptive.chart.ws.logic.WsChartContentController
import `fun`.adaptive.chart.ws.model.WsChartContext
import `fun`.adaptive.chart.ws.model.WsChartPaneData
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.generated.resources.menu
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneController
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
        this,
        "Chart",
        Graphics.menu,
        WsPanePosition.RightTop,
        WsChartContext.CHART_TOOL_PANE_KEY,
        data = Unit,
        controller = WsUnitPaneController(this)
    )

    addContentPaneBuilder(WsChartContext.WSIT_CHART_SERIES) { item ->

        WsPane(
            UUID(),
            this,
            item.name,
            Graphics.monitoring,
            WsPanePosition.Center,
            WsChartContext.CHART_CONTENT_PANE_KEY,
            data = WsChartPaneData(),
            controller = WsChartContentController(this)
        )

    }

    addItemConfig(WsChartContext.WSIT_CHART_SERIES, Graphics.database)

}
package `fun`.adaptive.chart

import `fun`.adaptive.chart.generated.resources.database
import `fun`.adaptive.chart.generated.resources.monitoring
import `fun`.adaptive.chart.ws.logic.WsChartContentController
import `fun`.adaptive.chart.ws.model.WsChartContext
import `fun`.adaptive.chart.ws.model.WsChartPaneData
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

class ChartWsModule<WT : Workspace> : ChartModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {

        contexts += WsChartContext(this)

//        + WsPane(
//            UUID(),
//            "Chart",
//            Graphics.menu,
//            WsPanePosition.RightTop,
//            WsChartContext.CHART_TOOL_PANE_KEY,
//            data = Unit,
//            controller = WsUnitPaneController()
//        )

        addContentPaneBuilder(WsChartContext.WSIT_CHART_SERIES) { item ->

            WsPane(
                UUID(),
                workspace,
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

}
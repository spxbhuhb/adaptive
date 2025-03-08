package `fun`.adaptive.chart.ws

import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.ui.workspace.model.Workspace
import `fun`.adaptive.ui.workspace.model.WorkspaceContext

class WsChartContext(
    override val workspace: Workspace
) : WorkspaceContext {

//    val panes = mutableListOf<wsChartPane>()

    val activePaneContexts = mutableListOf<WsPiChartSeries>()

    val activeRecipeKey = storeFor<String?> { null }

    fun openChart(item: WsPiChartSeries) {
//        context.activeRecipeKey.value = (item.data as? String) ?: return
//
//        workspace.center.value = workspace.panes.first { it.key == CHART_CONTENT_PANE_KEY }.uuid
    }

    companion object {
        const val CHART_TOOL_PANE_KEY = "chart:builder"
        const val CHART_CONTENT_PANE_KEY = "chart:view"
    }

}
package `fun`.adaptive.chart.ws

import `fun`.adaptive.chart.ws.WsChartContext.Companion.CHART_TOOL_PANE_KEY
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.workspace.model.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun wsChartToolPane() : AdaptiveFragment {

    val context = fragment().wsContext<WsChartContext>()
    //val items = context.projectItems.map { it.toTreeItem(context) }

    wsToolPane(context.pane(CHART_TOOL_PANE_KEY)) {
        //tree(items)
    }

    return fragment()
}
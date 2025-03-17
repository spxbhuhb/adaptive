package `fun`.adaptive.chart.ws

import `fun`.adaptive.chart.ws.model.WsChartContext
import `fun`.adaptive.chart.ws.model.WsChartContext.Companion.CHART_TOOL_PANE_KEY
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun wsChartToolPane(pane: WsPane<Unit>): AdaptiveFragment {

    val context = fragment().wsContext<WsChartContext>()

    val treeViewModel = TreeViewModel(
        context.items.map { it.toTreeItem(context, null) },
        selectedFun = { viewModel, item, modifiers -> if (item.children.isEmpty()) context.openChart(item.data, modifiers) },
        context = Unit
    )

    wsToolPane(pane) {
        tree(treeViewModel)
    }

    return fragment()
}
package `fun`.adaptive.chart.ws

import `fun`.adaptive.chart.ui.lineChart
import `fun`.adaptive.chart.ws.model.WsChartContext
import `fun`.adaptive.chart.ws.model.WsChartPaneData
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.model.WsPane

@Adaptive
fun wsChartContentPane(pane: WsPane<WsChartPaneData, *>): AdaptiveFragment {
    val items = valueFrom { pane.data.items }

    val context = fragment().wsContext<WsChartContext>()

    lineChart()

    return fragment()
}
package `fun`.adaptive.chart

import `fun`.adaptive.chart.ws.model.WsChartContext
import `fun`.adaptive.chart.ws.wsChartContentPane
import `fun`.adaptive.chart.ws.wsChartToolPane
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object ChartFragmentFactory : FoundationFragmentFactory() {
    init {
        add(WsChartContext.Companion.CHART_TOOL_PANE_KEY, ::wsChartToolPane)
        add(WsChartContext.Companion.CHART_CONTENT_PANE_KEY, ::wsChartContentPane)
    }
}
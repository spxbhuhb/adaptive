package `fun`.adaptive.chart

import `fun`.adaptive.chart.ws.WsChartContext
import `fun`.adaptive.chart.ws.wsChartContentPane
import `fun`.adaptive.chart.ws.wsChartToolPane
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

fun chartCommon() {

}

fun AdaptiveAdapter.chartCommon() {
    fragmentFactory += arrayOf(ChartFragmentFactory)
}

object ChartFragmentFactory : FoundationFragmentFactory() {
    init {
        add(WsChartContext.CHART_TOOL_PANE_KEY, ::wsChartToolPane)
        add(WsChartContext.CHART_CONTENT_PANE_KEY, ::wsChartContentPane)
    }
}
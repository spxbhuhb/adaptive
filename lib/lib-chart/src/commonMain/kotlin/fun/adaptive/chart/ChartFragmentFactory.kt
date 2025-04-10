package `fun`.adaptive.chart

import `fun`.adaptive.chart.ui.basicHorizontalAxis
import `fun`.adaptive.chart.ui.basicLineSeries
import `fun`.adaptive.chart.ui.basicVerticalAxis
import `fun`.adaptive.chart.ws.model.WsChartContext
import `fun`.adaptive.chart.ws.wsChartContentPane
import `fun`.adaptive.chart.ws.wsChartToolPane
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object ChartFragmentFactory : FoundationFragmentFactory() {
    init {
        add(WsChartContext.CHART_TOOL_PANE_KEY, ::wsChartToolPane)
        add(WsChartContext.CHART_CONTENT_PANE_KEY, ::wsChartContentPane)
        add(WsChartContext.BASIC_HORIZONTAL_AXIS, ::basicHorizontalAxis)
        add(WsChartContext.BASIC_VERTICAL_AXIS, ::basicVerticalAxis)
        add(WsChartContext.BASIC_LINE_SERIES, ::basicLineSeries)
    }
}
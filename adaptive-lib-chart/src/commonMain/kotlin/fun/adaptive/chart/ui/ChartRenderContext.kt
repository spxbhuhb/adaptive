package `fun`.adaptive.chart.ui

import `fun`.adaptive.chart.model.ChartRenderAxis
import `fun`.adaptive.chart.model.ChartRenderSeries

class ChartRenderContext<XT,YT>(
    val axes : List<ChartRenderAxis<XT,YT>>,
    val series : List<ChartRenderSeries>,
    val xRangeStart : XT,
    val xRangeEnd : XT,
    val yRangeStart : YT,
    val yRangeEnd : YT,
    val theme : ChartTheme
)
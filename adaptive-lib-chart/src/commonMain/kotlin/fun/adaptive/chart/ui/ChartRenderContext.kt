package `fun`.adaptive.chart.ui

import `fun`.adaptive.chart.model.ChartAxis
import `fun`.adaptive.chart.model.ChartSeries

class ChartRenderContext(
    val axes : List<ChartAxis>,
    val series : List<ChartSeries>,
    val theme : ChartTheme
)
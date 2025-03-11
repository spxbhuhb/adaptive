package `fun`.adaptive.chart.model

data class ChartDataRange<XT, YT>(
    val xStart: XT,
    val xEnd: XT,
    val yStart: YT,
    val yEnd: YT
)
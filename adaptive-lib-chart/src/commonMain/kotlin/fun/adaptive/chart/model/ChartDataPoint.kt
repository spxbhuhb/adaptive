package `fun`.adaptive.chart.model

class ChartDataPoint<XT, YT>(
    override val x: XT,
    override val y: YT
) : AbstractChartDataPoint<XT, YT>()
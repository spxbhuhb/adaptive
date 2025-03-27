package `fun`.adaptive.chart.model

import `fun`.adaptive.model.CartesianPoint

class ChartDataPoint<XT, YT>(
    override val x: XT,
    override val y: YT
) : CartesianPoint<XT, YT>()
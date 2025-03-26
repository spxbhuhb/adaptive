package `fun`.adaptive.chart.model

data class ChartDataRange<XT, YT>(
    val xStart: XT,
    val xEnd: XT,
    val yStart: YT,
    val yEnd: YT
) {

    companion object {

        fun <XT : Comparable<XT>, YT : Comparable<YT>> ChartDataRange<XT, YT>?.update(
            data: List<AbstractChartDataPoint<XT, YT>>
        ): ChartDataRange<XT, YT>? {

            var xStart: XT? = this?.xStart
            var xEnd: XT? = this?.xEnd
            var yStart: YT? = this?.yStart
            var yEnd: YT? = this?.yEnd

            val first = data.firstOrNull() ?: return this
            xStart = min(xStart, first.x)

            val last = data.lastOrNull() ?: return this
            xEnd = max(xEnd, last.x)

            for (point in data) {
                yStart = min(yStart, point.y)
                yEnd = max(yEnd, point.y)
            }

            return ChartDataRange(xStart, xEnd, yStart!!, yEnd!!)
        }

        fun <T : Comparable<T>> min(a: T?, b: T): T =
            when {
                a == null -> b
                a < b -> a
                else -> b
            }

        fun <T : Comparable<T>> max(a: T?, b: T): T =
            when {
                a == null -> b
                a > b -> a
                else -> b
            }

    }

}
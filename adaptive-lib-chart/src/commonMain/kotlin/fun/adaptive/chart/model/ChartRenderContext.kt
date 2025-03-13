package `fun`.adaptive.chart.model

import `fun`.adaptive.chart.normalization.ChartNormalizer
import `fun`.adaptive.chart.normalization.NullNormalizer
import `fun`.adaptive.chart.ui.ChartTheme

class ChartRenderContext<XT : Comparable<XT>, YT : Comparable<YT>>(
    val items: List<ChartItem<XT, YT>>,
    val axes: List<ChartRenderAxis<XT, YT>>,
    val itemOffsetX: Double,
    val itemOffsetY: Double,
    val normalizerFun: (ChartDataRange<XT, YT>) -> ChartNormalizer<XT, YT>,
    val theme: ChartTheme = ChartTheme()
) {

    val range: ChartDataRange<XT, YT>? = initRange()
    val normalizer: ChartNormalizer<XT, YT> = range?.let { normalizerFun(range) } ?: NullNormalizer<XT, YT>()

    fun initRange(): ChartDataRange<XT, YT>? {
        var xStart: XT? = null
        var xEnd: XT? = null
        var yStart: YT? = null
        var yEnd: YT? = null

        for (item in items) {
            val first = item.data.firstOrNull() ?: continue
            xStart = min(xStart, first.x)

            val last = item.data.lastOrNull() ?: continue
            xEnd = max(xEnd, last.x)

            for (point in item.data) {
                yStart = min(yStart, point.y)
                yEnd = max(yEnd, point.y)
            }
        }

        if (xStart == null || xEnd == null || yStart == null || yEnd == null) return null

        return ChartDataRange(xStart, xEnd, yStart, yEnd)
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
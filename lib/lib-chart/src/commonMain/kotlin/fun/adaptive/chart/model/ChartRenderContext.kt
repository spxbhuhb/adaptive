package `fun`.adaptive.chart.model

import `fun`.adaptive.chart.model.ChartDataRange.Companion.update
import `fun`.adaptive.chart.normalization.ChartNormalizer
import `fun`.adaptive.chart.normalization.NullNormalizer
import `fun`.adaptive.chart.ui.ChartTheme
import `fun`.adaptive.ui.fragment.layout.RawSurrounding

/**
 * @property   range  The data range on axes to show. Used for data normalization and the mark
 *                    generation. The context goes over all [items] and calculates the range based
 *                    on the values of points.
 *
 * @property   zeroY  The assumed minimum value on the Y axis. If there is an actual data value
 *                    that is smaller than [zeroY], that data value will be used instead.
 *                    This property makes it possible to start the chart from zero, instead of the
 *                    minimum data value.
 */
data class ChartRenderContext<XT : Comparable<XT>, YT : Comparable<YT>, AT>(
    val items: List<ChartItem<XT, YT, AT>>,
    val axes: List<ChartAxis<XT, YT, AT>>,
    val plotPadding: RawSurrounding,
    val zeroY: YT,
    val normalizerFun: (ChartDataRange<XT, YT>) -> ChartNormalizer<XT, YT>,
    val extendRangeFun: (ChartDataRange<XT, YT>) -> ChartDataRange<XT, YT> = { it },
    val plotSpaceRatio: Double = 0.9,
    val theme: ChartTheme = ChartTheme()
) {

    val range: ChartDataRange<XT, YT>?
    val normalizer: ChartNormalizer<XT, YT>

    init {
        val sourceRange = initRange()

        if (sourceRange == null) {
            range = null
            normalizer = NullNormalizer()
        } else {
            range = extendRangeFun(sourceRange)
            normalizer = normalizerFun(range)
        }
    }

    fun initRange(): ChartDataRange<XT, YT>? {
        var range : ChartDataRange<XT,YT>? = null

        for (chartItem in items) {
           range = range.update(chartItem.sourceData)
        }

        if (range == null) return null

        if (range.yStart <= zeroY) return range

        return range.copy(yStart = zeroY)
    }


}
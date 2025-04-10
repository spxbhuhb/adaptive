package `fun`.adaptive.chart.model

import `fun`.adaptive.chart.model.ChartDataRange.Companion.update
import `fun`.adaptive.chart.normalization.ChartNormalizer
import `fun`.adaptive.chart.normalization.NullNormalizer
import `fun`.adaptive.chart.ui.ChartTheme
import `fun`.adaptive.ui.fragment.layout.RawSurrounding

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
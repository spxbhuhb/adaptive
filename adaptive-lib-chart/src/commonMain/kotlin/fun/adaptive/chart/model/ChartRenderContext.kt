package `fun`.adaptive.chart.model

import `fun`.adaptive.chart.model.ChartDataRange.Companion.update
import `fun`.adaptive.chart.normalization.ChartNormalizer
import `fun`.adaptive.chart.normalization.NullNormalizer
import `fun`.adaptive.chart.ui.ChartTheme

class ChartRenderContext<XT : Comparable<XT>, YT : Comparable<YT>>(
    val items: List<ChartItem<XT, YT>>,
    val axes: List<ChartAxis<XT, YT>>,
    val itemOffsetX: Double,
    val itemOffsetY: Double,
    val normalizerFun: (ChartDataRange<XT, YT>) -> ChartNormalizer<XT, YT>,
    val theme: ChartTheme = ChartTheme()
) {

    val range: ChartDataRange<XT, YT>? = initRange()
    val normalizer: ChartNormalizer<XT, YT> = range?.let { normalizerFun(range) } ?: NullNormalizer<XT, YT>()

    fun initRange(): ChartDataRange<XT, YT>? {
        var range : ChartDataRange<XT,YT>? = null

        for (chartItem in items) {
           range = range.update(chartItem.sourceData)
        }

        return range
    }


}
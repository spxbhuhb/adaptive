package `fun`.adaptive.chart.calculation

import `fun`.adaptive.chart.model.ChartItem
import `fun`.adaptive.chart.normalization.ChartNormalizer

class CalculationContext<XT : Comparable<XT>, YT : Comparable<YT>, AT>(
    val start : XT,
    val end : XT,
    val normalizedInterval : Double,
    val normalizer : ChartNormalizer<XT, YT>,
    val calculation : (chartItem : ChartItem<XT, YT, AT>, start : Int, end : Int) -> YT?
) {
    val markers : List<XT?> = prepareMarkers()

    fun prepareMarkers() : List<XT?> {
        val out = mutableListOf<XT?>()

        val normalizer = this.normalizer
        val normalizedInterval = this.normalizedInterval

        var curPos = normalizer.normalizeX(this.start)
        val endPos = normalizer.normalizeX(this.end)

        if (! normalizedInterval.isFinite() || normalizedInterval <= 0.0 || ! curPos.isFinite() || ! endPos.isFinite() || curPos >= endPos) {
            return out
        }

        while (curPos < endPos) {
            out += normalizer.denormalizeX(curPos)
            curPos += normalizedInterval
        }

        return out
    }
}
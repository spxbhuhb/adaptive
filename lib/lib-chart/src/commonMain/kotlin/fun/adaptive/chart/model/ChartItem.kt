package `fun`.adaptive.chart.model

import `fun`.adaptive.chart.calculation.CalculationContext
import `fun`.adaptive.chart.normalization.ChartNormalizer
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.graphics.canvas.model.path.LineTo
import `fun`.adaptive.model.CartesianPoint

data class ChartItem<XT : Comparable<XT>, YT : Comparable<YT>, AT>(
    val renderKey: FragmentKey,
    val sourceData: List<CartesianPoint<XT, YT>>,
    val instructions: AdaptiveInstructionGroup = emptyInstructions,
    val attachment: AT? = null,
    val loading: Boolean = true
) {

    // these are used to check if operation recalculation is needed

    var lastWidth = 0.0
    var lastHeight = 0.0

    var lastXStart : XT? = null
    var lastXEnd : XT? = null

    var lastNormalizer : ChartNormalizer<XT, YT>? = null

    val normalizedData = mutableListOf<CartesianPoint<Double, Double>>()

    var operations = mutableListOf<LineTo>()

    val cells = mutableListOf<YT?>()

    fun normalize(normalizer: ChartNormalizer<XT, YT>): ChartItem<XT, YT, AT> {

        val out = normalizedData

        if (normalizer != lastNormalizer) {
            out.clear()
            lastNormalizer = normalizer
            lastWidth = Double.NaN
            lastHeight = Double.NaN
        }

        if (out.isNotEmpty()) return this

        for (point in sourceData) {
            out +=
                ChartDataPoint(
                    normalizer.normalizeX(point.x),
                    normalizer.normalizeY(point.y)
                )
        }

        return this
    }

    fun prepareOperations(width: Double, height: Double): ChartItem<XT, YT, AT> {

        val out = operations

        if (width != lastWidth || height != lastHeight) {
            out.clear()
            lastWidth = width
            lastHeight = height
        }

        if (out.isNotEmpty()) return this

        for (point in normalizedData) {
            out += LineTo(point.x * width, - point.y * height)
        }

        return this
    }

    fun prepareCells(
        config: CalculationContext<XT, YT, AT>
    ): ChartItem<XT, YT, AT> {

        val out = cells

        if (config.start != lastXStart || config.end != lastXEnd) {
            out.clear()
            lastXStart = config.start
            lastXEnd = config.end
        }

        if (out.isNotEmpty()) return this

        val normalizer = config.normalizer
        val normalizedInterval = config.normalizedInterval
        val calculation = config.calculation

        var index = 0
        var curPos = normalizer.normalizeX(config.start)
        val endPos = normalizer.normalizeX(config.end)

        if (! normalizedInterval.isFinite() || normalizedInterval <= 0.0 || ! curPos.isFinite() || ! endPos.isFinite() || curPos >= endPos) {
            return this
        }

        while (curPos < endPos) {
            var nextIndex = index
            val nextPos = curPos + normalizedInterval

            while (nextIndex < normalizedData.size && normalizedData[nextIndex].x < nextPos) {
                nextIndex ++
            }

            if (nextIndex != index) {
                out += calculation(this, index, nextIndex)
            } else {
                out += null
            }

            index = nextIndex
            curPos += normalizedInterval
        }

        return this
    }


}
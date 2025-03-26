package `fun`.adaptive.chart.model

import `fun`.adaptive.chart.calculation.CalculationContext
import `fun`.adaptive.chart.normalization.ChartNormalizer
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.graphics.canvas.model.path.LineTo

class ChartItem<XT : Comparable<XT>, YT : Comparable<YT>>(
    val renderKey: FragmentKey,
    val sourceData: List<ChartDataPoint<XT, YT>>,
    val instructions: AdaptiveInstructionGroup = emptyInstructions
) {

    val normalizedData = mutableListOf<ChartDataPoint<Double, Double>>()

    var operations = mutableListOf<LineTo>()

    val cells = mutableListOf<YT?>()

    fun normalize(normalizer: ChartNormalizer<XT, YT>): ChartItem<XT, YT> {

        val out = normalizedData
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

    fun prepareOperations(width: Double, height: Double): ChartItem<XT, YT> {

        val out = operations
        if (out.isNotEmpty()) return this

        for (point in normalizedData) {
            out += LineTo(point.x * width, - point.y * height)
        }

        return this
    }

    fun prepareCells(
        config : CalculationContext<XT,YT>
    ): ChartItem<XT, YT> {

        val out = cells
        if (out.isNotEmpty()) return this

        val normalizer = config.normalizer
        val normalizedInterval = config.normalizedInterval
        val calculation = config.calculation

        var index = 0
        var curPos = normalizer.normalizeX(config.start)
        val endPos = normalizer.normalizeX(config.end)

        while (curPos < endPos) {
            var nextIndex = index
            val nextPos = curPos + normalizedInterval

            while (nextIndex < normalizedData.size && normalizedData[nextIndex].x < nextPos) {
                nextIndex++
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
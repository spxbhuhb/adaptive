package `fun`.adaptive.chart.model

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.graphics.canvas.model.path.LineTo

class ChartItem<XT : Comparable<XT>, YT : Comparable<YT>>(
    val render: FragmentKey,
    val data: List<ChartDataPoint<XT, YT>>,
    val instructions: AdaptiveInstructionGroup = emptyInstructions
) {

    val normalizedData = mutableListOf<ChartDataPoint<Double, Double>>()

    fun normalize(context: ChartRenderContext<XT, YT>) {
        if (normalizedData.isNotEmpty()) return

        val normalizer = context.normalizer

        for (point in data) {

            normalizedData +=
                ChartDataPoint(
                    normalizer.normalizeX(point.x),
                    normalizer.normalizeY(point.y)
                )

        }

        normalizedData
    }

    fun lineTo(context: ChartRenderContext<*, *>, width: Double, height: Double): List<LineTo> {
        @Suppress("UNCHECKED_CAST") // couldn't figure out how to resolve this, function references does not support types
        context as ChartRenderContext<XT, YT>

        normalize(context)

        val out = mutableListOf<LineTo>()

        for (point in normalizedData) {
            out += LineTo(
                point.x * width,
                - point.y * height
            )
        }

        return out
    }
}
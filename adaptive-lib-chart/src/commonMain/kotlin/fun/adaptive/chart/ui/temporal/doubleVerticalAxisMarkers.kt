package `fun`.adaptive.chart.ui.temporal

import `fun`.adaptive.chart.model.ChartAxis
import `fun`.adaptive.chart.model.ChartMarker
import `fun`.adaptive.chart.model.ChartRenderContext
import `fun`.adaptive.chart.normalization.AbstractDoubleNormalizer
import `fun`.adaptive.ui.fragment.layout.RawSize
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

fun <YT : Comparable<YT>> doubleVerticalAxisMarkers(
    context: ChartRenderContext<*, YT, *>,
    @Suppress("unused") axis: ChartAxis<*, YT, *>,
    canvasSize: RawSize,
    labelTextFun: (YT) -> Double
): List<ChartMarker> {

    val availableHeight = canvasSize.height - context.plotPadding.top - context.plotPadding.bottom
    val approxLabelSpacing = 50.0

    val normalizer = context.normalizer as? AbstractDoubleNormalizer ?: return emptyList()

    calculateMarkerPositions(
        availableHeight,
        approxLabelSpacing,
        normalizer.yStart,
        normalizer.yEnd
    ).map { (offset, value) ->

        ChartMarker(
            offset = availableHeight - offset,
            labelText = value.toString(),
            guide = true
        )

    }.also {
        return it
    }
}

/**
 * @param   availableSize    available size (in pixels) to distribute markers in
 * @param   stepSize         desired step size
 * @param   start            the minimum value to show
 * @param   end              the maximum value to show
 *
 * @return  position (pixel) - value pairs
 */
fun calculateMarkerPositions(
    availableSize: Double,
    stepSize : Double,
    start: Double,
    end: Double
): List<Pair<Double, Double>> {
    val maxCount = availableSize / stepSize

    val yRange = end - start

    val yStep = niceNumber(yRange / maxCount, true)
    val offsetStep = yStep * availableSize / yRange

    var y = start
    var offset = 0.0

    val out = mutableListOf<Pair<Double, Double>>()

    while (offset < availableSize) {
        out += offset to y
        y += yStep
        offset += offsetStep
    }

    return out
}

fun niceNumber(range: Double, round: Boolean): Double {
    val exponent = floor(log10(range)) // 10^exponent
    val fraction = range / 10.0.pow(exponent)

    val niceFraction = when {
        round -> when {
            fraction < 1.5 -> 1.0
            fraction < 3.0 -> 2.0
            fraction < 7.0 -> 5.0
            else -> 10.0
        }

        else -> when {
            fraction <= 1.0 -> 1.0
            fraction <= 2.0 -> 2.0
            fraction <= 5.0 -> 5.0
            else -> 10.0
        }
    }
    return niceFraction * 10.0.pow(exponent)
}
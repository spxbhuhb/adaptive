package `fun`.adaptive.chart.ui.temporal

import `fun`.adaptive.chart.model.ChartAxis
import `fun`.adaptive.chart.model.ChartMarker
import `fun`.adaptive.chart.model.ChartRenderContext
import `fun`.adaptive.chart.normalization.AbstractDoubleNormalizer
import `fun`.adaptive.ui.fragment.layout.RawSize
import `fun`.adaptive.utility.format
import kotlin.math.abs
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

    val yStart = normalizer.yStart / context.plotSpaceRatio
    val yEnd = normalizer.yEnd / context.plotSpaceRatio

    // Determine the appropriate number of decimal places based on the data range
    val decimalPlaces = determineDecimalPlaces(yStart, yEnd)

    calculateMarkerPositions(
        availableHeight,
        approxLabelSpacing,
        yStart,
        yEnd
    ).map { (offset, value) ->

        ChartMarker(
            offset = availableHeight - offset,
            labelText = value.format(decimals = decimalPlaces, hideZeroDecimals = true),
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

/**
 * Determines the appropriate number of decimal places to display based on the range of values.
 *
 * @param min The minimum value in the range
 * @param max The maximum value in the range
 * @return The number of decimal places to display
 */
fun determineDecimalPlaces(min: Double, max: Double): Int {
    // Handle special cases
    if (min.isNaN() || max.isNaN() || min.isInfinite() || max.isInfinite()) {
        return 1 // Default to 1 decimal place for special cases
    }

    val range = abs(max - min)

    // For very small ranges, we need more precision
    return when {
        range == 0.0 -> 1 // Default to 1 decimal place if range is 0
        range >= 100 -> 0 // No decimals for large ranges
        range >= 10 -> 1 // 1 decimal for medium ranges
        range >= 1 -> 2 // 2 decimals for smaller ranges
        range >= 0.1 -> 3 // 3 decimals for very small ranges
        range >= 0.01 -> 4 // 4 decimals for tiny ranges
        range >= 0.001 -> 5 // 5 decimals for extremely small ranges
        else -> 6 // 6 decimals for microscopic ranges
    }
}
package `fun`.adaptive.chart.ui.temporal

import `fun`.adaptive.chart.model.ChartAxis
import `fun`.adaptive.chart.model.ChartRenderContext
import `fun`.adaptive.chart.model.ChartMarker
import `fun`.adaptive.chart.normalization.AbstractDoubleNormalizer
import `fun`.adaptive.ui.fragment.layout.RawSize
import `fun`.adaptive.utility.format

fun <YT : Comparable<YT>> doubleVerticalAxisMarkers(
    context: ChartRenderContext<*, YT, *>,
    @Suppress("unused") axis: ChartAxis<*, YT, *>,
    canvasSize: RawSize,
    labelTextFun : (YT) -> Double
): List<ChartMarker> {

    val itemsHeight = canvasSize.height - context.itemOffsetY
    val count = (itemsHeight / 50).toInt() - 1
    val step = 1.0 / count

    val normalizer = context.normalizer as? AbstractDoubleNormalizer ?: return emptyList()

    val yRange = normalizer.yRange
    val tickRange = yRange / count

    val out = mutableListOf<ChartMarker>()

    for (i in 1 .. count - 1) {
        val offset = i * step
        out += ChartMarker(
            offset = itemsHeight - offset * itemsHeight,
            tickSize = if (i % 2 == 0) 8.0 else 4.0,
            labelText = doubleLabelText(labelTextFun(normalizer.denormalizeY(offset)), tickRange),
            guide = true
        )
    }

    return out
}

fun doubleLabelText(value: Double?, tickRange: Double): String {
    if (value == null) return ""

    val decimals = when {
        tickRange >= 1 -> 0
        tickRange >= 0.1 -> 1
        tickRange >= 0.01 -> 2
        tickRange >= 0.001 -> 3
        else -> 4
    }

    return value.format(decimals)
}
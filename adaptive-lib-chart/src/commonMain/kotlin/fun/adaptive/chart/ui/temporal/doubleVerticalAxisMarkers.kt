package `fun`.adaptive.chart.ui.temporal

import `fun`.adaptive.chart.model.ChartAxis
import `fun`.adaptive.chart.model.ChartRenderContext
import `fun`.adaptive.chart.model.ChartMarker
import `fun`.adaptive.ui.fragment.layout.RawSize
import `fun`.adaptive.utility.format

fun doubleVerticalAxisMarkers(
    context : ChartRenderContext<*, Double, *>,
    @Suppress("unused") axis : ChartAxis<*, Double, *>,
    canvasSize : RawSize
) : List<ChartMarker> {
    val range = context.range ?: return emptyList()

    val itemsHeight = canvasSize.height - context.itemOffsetY
    val count = (itemsHeight / 50).toInt() - 1
    val step = 1.0 / count

    val normalizer = context.normalizer

    val yRange = range.yEnd - range.yStart
    val tickRange = yRange / count
    
    val out = mutableListOf<ChartMarker>()

    for (i in 1 .. count - 1) {
        val offset = i * step
        out += ChartMarker(
            offset = itemsHeight - offset * itemsHeight,
            tickSize = if (i % 2 == 0) 8.0 else 4.0,
            labelText =doubleLabelText(normalizer.denormalizeY(offset), tickRange),
            guide = true
        )
    }

    return out
}

fun doubleLabelText(value: Double?, tickRange: Double) : String {
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
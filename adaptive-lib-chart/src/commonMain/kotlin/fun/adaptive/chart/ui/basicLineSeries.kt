package `fun`.adaptive.chart.ui

import `fun`.adaptive.chart.model.ChartItem
import `fun`.adaptive.chart.model.ChartRenderContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.graphics.canvas.api.path
import `fun`.adaptive.graphics.canvas.api.translate
import `fun`.adaptive.graphics.canvas.model.path.MoveTo
import `fun`.adaptive.ui.fragment.layout.RawSize

@Adaptive
fun basicLineSeries(
    context: ChartRenderContext<*, *>,
    item: ChartItem<*, *>,
    canvasSize: RawSize
): AdaptiveFragment {

    val width = canvasSize.width - context.itemOffsetX
    val height = canvasSize.height - context.itemOffsetY
    val mapped = item.lineTo(context, width, height)
    val first = mapped.firstOrNull()

    if (first != null) {
        path(mapped, first.let { MoveTo(it.x, it.y) }) ..
            item.instructions ..
            translate(context.itemOffsetX, canvasSize.height - context.itemOffsetY)
    }

    return fragment()
}
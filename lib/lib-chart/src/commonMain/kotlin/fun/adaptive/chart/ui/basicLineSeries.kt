package `fun`.adaptive.chart.ui

import `fun`.adaptive.chart.model.ChartItem
import `fun`.adaptive.chart.model.ChartRenderContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.graphics.canvas.api.path
import `fun`.adaptive.graphics.canvas.api.translate
import `fun`.adaptive.graphics.canvas.model.path.LineTo
import `fun`.adaptive.graphics.canvas.model.path.MoveTo
import `fun`.adaptive.ui.fragment.layout.RawSize

@Adaptive
fun  basicLineSeries(
    context: ChartRenderContext<*, *, *>,
    item: ChartItem<*, *, *>,
    canvasSize: RawSize
): AdaptiveFragment {

    val operations = operations(context, item, canvasSize)
    val first = operations.firstOrNull()

    if (first != null) {
        path(operations, first.let { MoveTo(it.x, it.y) }) ..
            item.instructions ..
            translate(context.plotPadding.start, canvasSize.height - context.plotPadding.bottom)
    }

    return fragment()
}

private fun <XT : Comparable<XT>, YT : Comparable<YT>,AT> operations(
    context : ChartRenderContext<XT, YT, AT>,
    item : ChartItem<*, *, *>,
    canvasSize : RawSize
): MutableList<LineTo> {
    // Can't be helped for now the problem is that function references does not allow type parameters.
    // Therefore, the generic, fragment factory line series function cannot have type parameters.

    @Suppress("UNCHECKED_CAST")
    item as ChartItem<XT, YT, AT>

    val width = canvasSize.width - context.plotPadding.start
    val height = (canvasSize.height - context.plotPadding.bottom) * context.plotSpaceRatio
    return item.normalize(context.normalizer).prepareOperations(width, height).operations
}
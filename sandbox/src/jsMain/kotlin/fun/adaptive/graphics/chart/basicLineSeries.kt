package `fun`.adaptive.graphics.chart

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.canvas.api.*
import `fun`.adaptive.graphics.canvas.model.path.LineTo
import `fun`.adaptive.graphics.canvas.model.path.MoveTo
import `fun`.adaptive.graphics.chart.model.ChartAxis
import `fun`.adaptive.graphics.chart.model.ChartSeries
import `fun`.adaptive.ui.api.popupAlign
import `fun`.adaptive.ui.instruction.layout.Orientation

@Adaptive
fun basicLineSeries(context: ChartRenderContext, series: ChartSeries): AdaptiveFragment {

    val first = series.points.firstOrNull()

    if (first != null) {
        path(series.points.map { LineTo(it.x, - it.y) }, MoveTo(first.x, - first.y)) .. stroke(series.color)
    }

    return fragment()
}
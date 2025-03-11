package `fun`.adaptive.chart.ui

import `fun`.adaptive.chart.model.ChartRenderSeries
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.graphics.canvas.api.path
import `fun`.adaptive.graphics.canvas.api.stroke
import `fun`.adaptive.graphics.canvas.api.translate
import `fun`.adaptive.graphics.canvas.model.path.LineTo
import `fun`.adaptive.graphics.canvas.model.path.MoveTo
import `fun`.adaptive.ui.fragment.layout.RawSize

@Adaptive
fun basicLineSeries(context: ChartRenderContext<*,*>, series: ChartRenderSeries, canvasSize : RawSize): AdaptiveFragment {

    val first = series.points.firstOrNull()

    if (first != null) {
        path(series.points.map { LineTo(it.x, - it.y) }, MoveTo(first.x, - first.y)) ..
            stroke(series.color) ..
            translate(series.offsetX, canvasSize.height - series.offsetY)
    }

    return fragment()
}
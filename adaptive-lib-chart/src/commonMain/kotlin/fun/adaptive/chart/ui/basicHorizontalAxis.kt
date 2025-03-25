package `fun`.adaptive.chart.ui

import `fun`.adaptive.chart.model.ChartRenderAxis
import `fun`.adaptive.chart.model.ChartRenderContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.graphics.canvas.api.fillText
import `fun`.adaptive.graphics.canvas.api.line
import `fun`.adaptive.graphics.canvas.api.transform
import `fun`.adaptive.graphics.canvas.api.translate
import `fun`.adaptive.ui.api.popupAlign
import `fun`.adaptive.ui.fragment.layout.RawSize

@Adaptive
fun basicHorizontalAxis(
    context: ChartRenderContext<*, *>,
    axis: ChartRenderAxis<*,*>,
    canvasSize: RawSize
): AdaptiveFragment {
    val markers = axis.markers(context, canvasSize)
    val tickMax = markers.maxOfOrNull { it.tickSize ?: 0.0 } ?: 0.0
    val axisOffset = canvasSize.height - axis.size

    transform(translate(axis.offset, axisOffset)) {

        if (axis.axisLine) {
            line(- 1.0, 0.0, canvasSize.width - axis.offset, 0.0) .. context.theme.axisLine
        }

        for (marker in markers) {
            if (marker.tickSize != null) {
                line(marker.offset, 0.0, marker.offset, 0.0 + marker.tickSize) .. context.theme.axisLine
            }
            if (marker.labelText != null) {
                fillText(marker.offset, tickMax + 2.0 + marker.labelOffset, marker.labelText, popupAlign.belowCenter) .. context.theme.axisLabel .. marker.labelInstructions
            }
            if (marker.guide) {
                line(marker.offset, 0.0, marker.offset, -axisOffset) .. context.theme.axisGuide
            }
        }
    }

    return fragment()
}
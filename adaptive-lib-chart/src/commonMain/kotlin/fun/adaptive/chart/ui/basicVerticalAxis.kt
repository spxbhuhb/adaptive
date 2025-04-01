package `fun`.adaptive.chart.ui

import `fun`.adaptive.chart.model.ChartAxis
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
fun basicVerticalAxis(
    context: ChartRenderContext<*, *, *>,
    axis: ChartAxis<*, *, *>,
    canvasSize: RawSize
): AdaptiveFragment {
    val axisHeight = canvasSize.height - axis.offset
    val markers = axis.markers(context, canvasSize)
    val tickMax = markers.maxOfOrNull { it.tickSize ?: 0.0 } ?: 0.0
    val guideSize = canvasSize.width - axis.size

    transform(translate(axis.size, 0.0)) {

        if (axis.axisLine) {
            line(0.0, 0.0, 0.0, axisHeight + 1) .. context.theme.axisLine
        }

        for (marker in markers) {
            if (marker.tickSize != null) {
                line(0.0, marker.offset, 0.0 - marker.tickSize, marker.offset) .. context.theme.axisLine
            }

            // TODO axis label hiding algorithm
            // The > 12.0 condition hopefully hides the label when it would be cut. This should be
            // label size dependent.
            if (marker.labelText != null && (marker.offset - 4.0 > 12.0)) {
                // FIXME manual offset corrections in chart axes
                fillText(- tickMax - 10.0, marker.offset - 4.0, marker.labelText, popupAlign.beforeCenter) .. context.theme.axisLabel .. marker.labelInstructions
            }

            if (marker.guide) {
                line(0.0, marker.offset, guideSize, marker.offset) .. context.theme.axisGuide
            }
        }

    }

    return fragment()
}
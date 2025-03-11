package `fun`.adaptive.chart.ui

import `fun`.adaptive.chart.model.ChartAxis
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
fun basicVerticalAxis(context: ChartRenderContext, axis: ChartAxis, canvasSize: RawSize): AdaptiveFragment {
    val axisHeight = canvasSize.height - axis.offset
    val ticks = ticks50100(axisHeight, reverse = true)
    val tickMax = ticks.maxOfOrNull { it.size } ?: 0.0

    transform(translate(axis.size, 0.0)) {

        if (axis.axisLine) {
            line(0.0, 0.0, 0.0, axisHeight + 1) .. context.theme.axisLine
        }

        for (tick in ticks) {
            line(0.0, tick.offset, 0.0 - tick.size, tick.offset) .. context.theme.axisLine
        }

        for (label in labels50100(axisHeight, reverse = true)) {
            fillText(- tickMax - 2.0, label.offset + 1.0, label.text, popupAlign.beforeCenter) .. context.theme.axisLabel .. label.instructions
        }

        for (guide in guides50100(axisHeight, canvasSize.width, reverse = true)) {
            line(0.0, guide.offset, guide.size, guide.offset) .. context.theme.axisGuide
        }

    }

    return fragment()
}
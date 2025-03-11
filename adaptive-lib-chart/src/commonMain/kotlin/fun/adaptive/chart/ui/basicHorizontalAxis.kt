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
fun basicHorizontalAxis(context: ChartRenderContext, axis: ChartAxis, canvasSize: RawSize): AdaptiveFragment {
    val ticks = ticks50100(canvasSize.width, reverse = false)
    val tickMax = ticks.maxOfOrNull { it.size } ?: 0.0

    transform(translate(axis.offset, canvasSize.height - axis.size)) {

        if (axis.axisLine) {
            line(- 1.0, 0.0, canvasSize.width - axis.offset, 0.0) .. context.theme.axisLine
        }

        for (tick in ticks) {
            line(tick.offset, 0.0, tick.offset, 0.0 + tick.size) .. context.theme.axisLine
        }

        for (label in labels50100(canvasSize.width, reverse = false)) {
            fillText(label.offset, tickMax + 2.0, label.text, popupAlign.belowCenter) .. context.theme.axisLabel .. label.instructions
        }

        for (guide in guides50100(canvasSize.width, canvasSize.height, reverse = false)) {
            line(guide.offset, 0.0, guide.offset, - guide.size) .. context.theme.axisGuide
        }

    }

    return fragment()
}
package `fun`.adaptive.chart.ui

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.canvas.api.fillText
import `fun`.adaptive.graphics.canvas.api.line
import `fun`.adaptive.graphics.canvas.api.transform
import `fun`.adaptive.chart.model.ChartAxis
import `fun`.adaptive.ui.api.popupAlign
import `fun`.adaptive.ui.instruction.layout.Orientation

@Adaptive
fun basicAxis(context : ChartRenderContext, axis: ChartAxis) : AdaptiveFragment {
    if (axis.orientation == Orientation.Horizontal) {
        basicHorizontalAxis(context, axis) .. instructions()
    } else {
        basicVerticalAxis(context, axis) .. instructions()
    }
    return fragment()
}

@Adaptive
fun basicHorizontalAxis(context : ChartRenderContext, axis: ChartAxis): AdaptiveFragment {
    val tickMax = axis.ticks.maxOfOrNull { it.size } ?: 0.0

    transform(instructions()) {

        if (axis.axisLine) {
            line(- 1.0, 0.0, axis.size, 0.0) .. context.theme.axisLine
        }

        for (tick in axis.ticks) {
            line(tick.offset, 0.0, tick.offset, 0.0 + tick.size) .. context.theme.axisLine
        }

        for (label in axis.labels) {
            fillText(label.offset, tickMax + 2.0, label.text, popupAlign.belowCenter) .. context.theme.axisLabel .. label.instructions
        }

        for (guide in axis.guides) {
            line(guide.offset, 0.0, guide.offset, - guide.size) .. context.theme.axisGuide
        }

    }

    return fragment()
}

@Adaptive
fun basicVerticalAxis(context : ChartRenderContext, axis: ChartAxis): AdaptiveFragment {
    val tickMax = axis.ticks.maxOfOrNull { it.size } ?: 0.0

    transform(instructions()) {

        if (axis.axisLine) {
            line(0.0, 0.0, 0.0, axis.size + 1) .. context.theme.axisLine
        }

        for (tick in axis.ticks) {
            line(0.0, tick.offset, 0.0 - tick.size, tick.offset) .. context.theme.axisLine
        }

        for (label in axis.labels) {
            fillText(- tickMax - 2.0, label.offset + 1.0, label.text, popupAlign.beforeCenter) .. context.theme.axisLabel .. label.instructions
        }

        for (guide in axis.guides) {
            line(0.0, guide.offset, guide.size, guide.offset) .. context.theme.axisGuide
        }

    }

    return fragment()
}
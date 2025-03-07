package `fun`.adaptive.graphics.chart

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.canvas.api.*
import `fun`.adaptive.graphics.chart.model.ChartAxis
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

        line(0.0, 0.5, axis.size, 0.5) .. context.theme.axisLine

        for (tick in axis.ticks) {
            line(tick.offset, 1.0, tick.offset, 1.0 + tick.size) .. context.theme.axisLine
        }

        for (label in axis.labels) {
            fillText(label.offset, tickMax + 2.0, label.text, popupAlign.belowCenter) .. context.theme.axisLabel
        }

    }

    return fragment()
}

@Adaptive
fun basicVerticalAxis(context : ChartRenderContext, axis: ChartAxis): AdaptiveFragment {
    val tickMax = axis.ticks.maxOfOrNull { it.size } ?: 0.0

    transform(instructions()) {

        line(0.5, 0.0, 0.5, axis.size) .. context.theme.axisLine

        for (tick in axis.ticks) {
            line(0.0, tick.offset, 0.0 - tick.size, tick.offset) .. context.theme.axisLine
        }

        for (label in axis.labels) {
            fillText(- tickMax - 2.0, label.offset + 1.0, label.text, popupAlign.beforeCenter) .. context.theme.axisLabel
        }

    }

    return fragment()
}
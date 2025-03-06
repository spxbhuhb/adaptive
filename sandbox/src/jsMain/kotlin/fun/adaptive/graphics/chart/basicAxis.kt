package `fun`.adaptive.graphics.chart

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.traceAll
import `fun`.adaptive.graphics.canvas.api.*
import `fun`.adaptive.graphics.canvas.path.ClosePath
import `fun`.adaptive.graphics.canvas.path.LineTo
import `fun`.adaptive.graphics.canvas.path.MoveTo
import `fun`.adaptive.graphics.canvas.transform.Translate
import `fun`.adaptive.graphics.chart.model.Axis
import `fun`.adaptive.ui.instruction.layout.Orientation

@Adaptive
fun basicAxis(axis: Axis) {
    translate(30.0, 30.0) {
        if (axis.orientation == Orientation.Horizontal) {
            basicHorizontalAxis(axis)
        }
    }
}

@Adaptive
fun basicHorizontalAxis(axis: Axis): AdaptiveFragment {
    val tickMax = axis.ticks.maxOfOrNull { it.size } ?: 0.0

    line(0.0, 0.5, axis.size, 0.5) .. stroke(0x0000ff)

    for (tick in axis.ticks) {
        line(tick.offset, 1.0, tick.offset, 1.0 + tick.size) .. stroke(0x0000ff)
    }

    for (label in axis.labels) {
        fillText(label.offset, tickMax + 1.0, label.text) .. fill(0x0000ff)
    }

    return fragment()
}


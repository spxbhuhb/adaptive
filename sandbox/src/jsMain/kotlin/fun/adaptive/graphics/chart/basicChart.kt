package `fun`.adaptive.graphics.chart

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.traceAll
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.canvas.api.fillText
import `fun`.adaptive.graphics.chart.model.Axis
import `fun`.adaptive.graphics.chart.model.Label
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.margin
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.theme.borders


val xAxis = Axis(
    Orientation.Horizontal,
    400.0,
    Label(0.0, "X axis", 0.0),
    listOf(),
    listOf(),
)

@Adaptive
fun basicChart() {
    box {
        size(422.dp, 422.dp) .. borders.outline .. margin { 10.dp }

        canvas {
            fillText(40.0, 40.0, "(40,40)") .. fill(0xff00ff)
            basicAxis(xAxis)
        }
    }
}
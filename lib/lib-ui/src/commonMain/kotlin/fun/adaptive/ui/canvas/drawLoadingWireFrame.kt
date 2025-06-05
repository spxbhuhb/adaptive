package `fun`.adaptive.ui.canvas

import `fun`.adaptive.graphics.canvas.platform.ActualCanvas
import `fun`.adaptive.ui.instruction.decoration.Color

fun ActualCanvas.drawLoadingWireFrame(
    color : Color,
    spacing : Double
) {

    setStroke(color) // Semi-transparent black for all lines

    var y = spacing
    while (height - y > 0.0) {
        line(0.0, y, width, y)
        y += spacing
    }

    var x = spacing
    while (width - x > 0.0) {
        line(x, 0.0, x, height)
        x += spacing
    }
}


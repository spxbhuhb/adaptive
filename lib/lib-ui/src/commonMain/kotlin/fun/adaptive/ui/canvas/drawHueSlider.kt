package `fun`.adaptive.ui.canvas

import `fun`.adaptive.graphics.canvas.model.gradient.GradientStop
import `fun`.adaptive.graphics.canvas.model.gradient.LinearGradient
import `fun`.adaptive.graphics.canvas.platform.ActualCanvas
import `fun`.adaptive.ui.instruction.decoration.Color

fun ActualCanvas.drawHueSlider(width: Double, height: Double, top: Double) {

    setFill(
        LinearGradient(
            0.0, 0.0, width, height,
            listOf(
                GradientStop(0.0, Color.decodeFromHsl(0.0, 1.0, 0.5)),    // Red
                GradientStop(1.0 / 6.0, Color.decodeFromHsl(60.0, 1.0, 0.5)),  // Yellow
                GradientStop(2.0 / 6.0, Color.decodeFromHsl(120.0, 1.0, 0.5)), // Green
                GradientStop(3.0 / 6.0, Color.decodeFromHsl(180.0, 1.0, 0.5)), // Cyan
                GradientStop(4.0 / 6.0, Color.decodeFromHsl(240.0, 1.0, 0.5)), // Blue
                GradientStop(5.0 / 6.0, Color.decodeFromHsl(300.0, 1.0, 0.5)), // Magenta
                GradientStop(1.0, Color.decodeFromHsl(360.0, 1.0, 0.5))   // Red again (wrap around)
            )
        )
    )

    fillRect(0.0, top, width, height)

}
package `fun`.adaptive.ui.canvas

import `fun`.adaptive.graphics.canvas.model.gradient.GradientStop
import `fun`.adaptive.graphics.canvas.model.gradient.LinearGradient
import `fun`.adaptive.graphics.canvas.platform.ActualCanvas
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.theme.colors

fun ActualCanvas.drawFakeHslColorPlane(width: Double, height: Double, baseHue: Double) {

    // Fill with base hue (fully saturated, 50% lightness)
    setFill(Color.decodeFromHsl(baseHue, 1.0, 0.5))

    fillRect(0.0, 0.0, width, height)

    // Overlay: White Gradient Left → Right
    setFill(
        LinearGradient(
            0.0, 0.0, width, 0.0,
            listOf(
                GradientStop(0.0, colors.white),
                GradientStop(1.0, colors.transparent)
            )
        )
    )

    fillRect(0.0, 0.0, width, height)

    // Overlay: Black Gradient Top → Bottom

    setFill(
        LinearGradient(
            0.0, 0.0, 0.0, height,
            listOf(
                GradientStop(0.0, colors.transparent),
                GradientStop(1.0, colors.black)
            )
        )
    )

    fillRect(0.0, 0.0, width, height)

}
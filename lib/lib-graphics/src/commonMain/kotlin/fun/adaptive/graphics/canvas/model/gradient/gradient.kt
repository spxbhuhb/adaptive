package `fun`.adaptive.graphics.canvas.model.gradient

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.ui.instruction.decoration.Color

@Adat
class GradientStop(
    val position: Double, // 0.0 to 1.0
    val color: Color
)

sealed interface Gradient

@Adat
class LinearGradient(
    val x0: Double,
    val y0: Double,
    val x1: Double,
    val y1: Double,
    val stops: List<GradientStop>
) : Gradient

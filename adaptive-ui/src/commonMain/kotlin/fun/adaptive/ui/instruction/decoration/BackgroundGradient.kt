package `fun`.adaptive.ui.instruction.decoration

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.render.decoration
import kotlin.math.PI
import kotlin.math.atan2

@Adat
class BackgroundGradient(
    val startPosition: Position,
    val endPosition: Position,
    val start: Color,
    val end: Color
) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        decoration(subject) { it.backgroundGradient = this }
    }

    val degree
        get() = asAngle(startPosition, endPosition)

    fun asAngle(startPoint: Position, endPosition: Position): Double {
        val dx = endPosition.left.value - startPoint.left.value
        val dy = endPosition.top.value - startPoint.top.value

        // Calculate the angle in radians
        val angleRadians = atan2(dy, dx)

        // Convert the angle to degrees
        val angleDegrees = angleRadians * 180 / PI

        // Adjust the angle to match CSS coordinate system
        // CSS angles are clockwise from the top (0 degrees)
        var angle = (90 - angleDegrees) % 360
        if (angle < 0) {
            angle += 360
        }

        return angle
    }

    companion object {
        val TOP = Position(0.5.dp, 0.0.dp)
        val BOTTOM = Position(0.5.dp, 1.0.dp)
        val LEFT = Position(0.0.dp, 0.5.dp)
        val RIGHT = Position(1.0.dp, 0.5.dp)
    }
}
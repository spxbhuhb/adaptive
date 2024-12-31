package `fun`.adaptive.graphics.canvas.api

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.graphics.canvas.canvas
import `fun`.adaptive.resource.graphics.GraphicsResource
import kotlin.math.PI

@AdaptiveExpect(canvas)
fun canvas(vararg instructions: AdaptiveInstruction, @Adaptive content: () -> Unit) {
    manualImplementation(instructions, content)
}

@AdaptiveExpect(canvas)
fun fillText(
    x: Double,
    y: Double,
    text: String,
    vararg instructions: AdaptiveInstruction
) : AdaptiveFragment {
    manualImplementation(x, y, text, instructions)
}

@AdaptiveExpect(canvas)
fun circle(
    centerX: Double,
    centerY: Double,
    radius: Double,
    startAngle: Double = 0.0,
    endAngle: Double = 2 * PI,
    anticlockwise: Boolean = false,
    vararg instructions: AdaptiveInstruction
) : AdaptiveFragment {
    manualImplementation(centerX, centerY, radius, startAngle, endAngle, anticlockwise, instructions)
}

@AdaptiveExpect(canvas)
fun line(
    x1: Double,
    y1: Double,
    x2: Double,
    y2: Double,
    vararg instructions: AdaptiveInstruction
) : AdaptiveFragment {
    manualImplementation(x1, y1, x2, y2, instructions)
}

@AdaptiveExpect(canvas)
fun svg(resource : GraphicsResource, vararg instructions: AdaptiveInstruction) {
    manualImplementation(resource, instructions)
}
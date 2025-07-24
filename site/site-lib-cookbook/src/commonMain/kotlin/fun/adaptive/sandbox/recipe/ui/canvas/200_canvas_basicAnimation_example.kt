package `fun`.adaptive.sandbox.recipe.ui.canvas

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.producer.poll
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.graphics.canvas.api.circle
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.canvas.api.fillText
import `fun`.adaptive.graphics.canvas.api.line
import `fun`.adaptive.graphics.canvas.api.rotate
import `fun`.adaptive.graphics.canvas.api.stroke
import `fun`.adaptive.graphics.canvas.api.transform
import `fun`.adaptive.graphics.canvas.api.translate
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders
import kotlin.math.PI
import kotlin.time.Clock.System.now
import kotlin.time.Duration.Companion.milliseconds

/**
 * # Basic animation
 */
@Adaptive
fun canvasBasicAnimationExample() : AdaptiveFragment {
    val seconds = poll(50.milliseconds) { (now().toEpochMilliseconds() % 3600).toDouble() } ?: 0.0

    box {
        size(402.dp, 402.dp) .. borders.outline

        canvas {
            maxSize

            circle(100.0, 100.0, seconds / 30.0) .. fill(Color(0x00ff00u, 0.3))
            transform {
                translate(100.0, 100.0) .. rotate((seconds / 10) * PI / 180, 0.0, 0.0)
                fillText(0.0, 0.0, "Canvas") .. fill(0xff00ff)
            }
            line(200.0, 200.0, 400.0, 400.0) .. stroke(0x0000ff)
        }
    }

    return fragment()
}
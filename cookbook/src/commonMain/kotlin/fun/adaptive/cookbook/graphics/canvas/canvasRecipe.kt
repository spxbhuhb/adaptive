package `fun`.adaptive.cookbook.graphics.canvas

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.producer.poll
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.graphics.canvas.api.circle
import `fun`.adaptive.graphics.svg.api.svgFill
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.milliseconds

@Adaptive
fun canvasRecipe() {
    val seconds = poll(50.milliseconds) { (now().toEpochMilliseconds() % 10000).toDouble() } ?: 0.0

    box {
        size(400.dp, 400.dp) .. borders.outline

        canvas {
            circle(100.0, 100.0, seconds / 100.0) .. svgFill(0xff0000)
        }
    }
}
package `fun`.adaptive.doc.example.canvas

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.graphics.canvas.api.*
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

/**
 * # Draw a line
 *
 * - Use the [canvasLine](fragment://) fragment to draw a line between two points.
 * - Use [stroke](instruction://) to define a stroke color.
 */
@Adaptive
fun canvasLineExample(): AdaptiveFragment {

    canvas {
        size(220.dp) .. borders.outline  .. backgrounds.surface

        line(x1 = 50.0, y1 = 50.0, x2 = 150.0, y2 = 50.0) .. stroke(colors.friendly)
        line(x1 = 150.0, y1 = 50.0, x2 = 150.0, y2 = 150.0) .. stroke(colors.warning)
        line(x1 = 150.0, y1 = 150.0, x2 = 50.0, y2 = 150.0) .. stroke(colors.danger)
        line(x1 = 50.0, y1 = 150.0, x2 = 50.0, y2 = 50.0) .. stroke(colors.info)
    }

    return fragment()
}
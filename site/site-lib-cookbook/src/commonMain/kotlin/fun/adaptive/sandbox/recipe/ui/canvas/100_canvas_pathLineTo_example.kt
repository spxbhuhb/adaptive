package `fun`.adaptive.sandbox.recipe.ui.canvas

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.graphics.canvas.api.*
import `fun`.adaptive.graphics.canvas.model.path.LineTo
import `fun`.adaptive.graphics.canvas.model.path.MoveTo
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

/**
 * # Draw a line with a path
 *
 * - use the [path](fragment://) Canvas fragment to draw a path on a canvas
 * - use the [MoveTo](class://) command to position to the starting point
 * - use the [LineTo](class://) command to draw a line from the last point to a new one
 * - a consequent [LineTo](class://) uses the end point of the previous command
 *
 * **IMPORTANT** [MoveTo](class://) parameters are absolute.
 *
 * **IMPORTANT** [LineTo](class://) parameters are absolute.
 */
@Adaptive
fun canvasPathLineToExample(): AdaptiveFragment {

    canvas {
        size(220.dp) .. borders.outline  .. backgrounds.surface

        path(
            listOf(
                MoveTo(50.0, 50.0),
                LineTo(x = 150.0, y = 50.0),
                LineTo(x = 150.0, y = 150.0),
                LineTo(x = 50.0, y = 150.0),
                LineTo(x = 50.0, y = 50.0)
            )
        ) .. stroke(colors.onSurfaceFriendly)
    }

    return fragment()
}
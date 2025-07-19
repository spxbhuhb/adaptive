package `fun`.adaptive.sandbox.recipe.ui.canvas

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.graphics.canvas.api.*
import `fun`.adaptive.graphics.canvas.model.path.LineTo
import `fun`.adaptive.graphics.canvas.model.path.MoveTo
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

/**
 * # Draw a rectangle with a path
 */
@Adaptive
fun canvasPathRectangleExample(): AdaptiveFragment {

    canvas {
        size(110.dp) .. borders.outline

        path(
            listOf(
                MoveTo(10.0, 10.0),
                LineTo(100.0, 10.0),
                LineTo(100.0, 100.0),
                LineTo(10.0, 100.0),
            )
        ) .. fill(colors.friendly)
    }

    return fragment()
}
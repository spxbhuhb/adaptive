package `fun`.adaptive.sandbox.recipe.ui.canvas

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.canvas.api.path
import `fun`.adaptive.graphics.canvas.model.path.Arc
import `fun`.adaptive.graphics.canvas.model.path.LineTo
import `fun`.adaptive.graphics.canvas.model.path.MoveTo
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors
import kotlin.math.*
/**
 * # Draw arcs with a path
 */
@Adaptive
fun canvasPathArcExample(): AdaptiveFragment {
    flowBox {
        canvas {
            size(600.dp) .. borders.outline

            path(
                listOf(
                    MoveTo(300.0, 200.0),
                    LineTo(150.0, 200.0),
                    Arc(
                        rx = 100.0,
                        ry = 150.0,
                        xAxisRotation = 30.0,
                        largeArcFlag = 1,
                        sweepFlag = 0,
                        x1 = 150.0,
                        y1 = 200.0,
                        x2 = 300.0,
                        y2 = 50.0
                    ),
                    LineTo(300.0, 200.0),
                )
            ) .. fill(colors.friendly)
        }
    }

    return fragment()
}
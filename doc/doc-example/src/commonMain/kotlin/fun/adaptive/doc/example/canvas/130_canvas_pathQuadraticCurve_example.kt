package `fun`.adaptive.doc.example.canvas

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.graphics.canvas.api.path
import `fun`.adaptive.graphics.canvas.api.stroke
import `fun`.adaptive.graphics.canvas.model.path.MoveTo
import `fun`.adaptive.graphics.canvas.model.path.QuadraticCurve
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

/**
 * # Draw a quadratic curve with a path
 *
 * - Use the [path](fragment://) Canvas fragment to draw a path on a canvas.
 * - Use the [QuadraticCurve](class://) path command to draw a quadratic curve.
 * - [QuadraticCurve](class://) implements the SVG quadratic curve.
 * - The curve starts from the end point of the last command.
 *
 * **IMPORTANT** [QuadraticCurve](class://) parameters are absolute.
 */
@Adaptive
fun canvasPathQuadraticCurveExample(): AdaptiveFragment {

    canvas {
        size(200.dp) .. borders.outline .. backgrounds.surface

        path(
            listOf(
                MoveTo(50.0, 50.0),

                QuadraticCurve(
                    x1 = 50.0,
                    y1 = 150.0,
                    x = 150.0,
                    y = 150.0
                )
            )
        ) .. stroke(colors.onSurfaceFriendly)
    }

    return fragment()
}
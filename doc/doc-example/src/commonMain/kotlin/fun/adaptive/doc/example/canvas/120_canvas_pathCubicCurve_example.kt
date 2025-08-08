package `fun`.adaptive.doc.example.canvas

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.graphics.canvas.api.path
import `fun`.adaptive.graphics.canvas.api.stroke
import `fun`.adaptive.graphics.canvas.model.path.CubicCurve
import `fun`.adaptive.graphics.canvas.model.path.LineTo
import `fun`.adaptive.graphics.canvas.model.path.MoveTo
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

/**
 * # Draw a cubic curve with a path
 *
 * - Use the [path](fragment://) Canvas fragment to draw a path on a canvas.
 * - Use the [CubicCurve](class://) path command to draw a cubic curve.
 * - [CubicCurve](class://) implements the SVG cubic curve.
 * - The curve starts from the end point of the last command.
 *
 * **IMPORTANT** [CubicCurve](class://) parameters are absolute.
 */
@Adaptive
fun canvasPathCubicCurveExample(): AdaptiveFragment {

    canvas {
        size(200.dp) .. borders.outline .. backgrounds.surface

        path(
            listOf(
                MoveTo(50.0, 50.0),

                LineTo(100.0, 50.0),

                CubicCurve(
                    x1 = 140.0,
                    y1 = 50.0,
                    x2 = 140.0,
                    y2 = 100.0,
                    x = 100.0,
                    y = 100.0
                ),

                LineTo(50.0, 100.0),

                CubicCurve(
                    x1 = 10.0,
                    y1 = 100.0,
                    x2 = 10.0,
                    y2 = 50.0,
                    x = 50.0,
                    y = 50.0
                )
            )
        ) .. stroke(colors.onSurfaceFriendly)
    }

    return fragment()
}
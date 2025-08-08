package `fun`.adaptive.doc.example.canvas

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.canvas.api.path
import `fun`.adaptive.graphics.canvas.api.stroke
import `fun`.adaptive.graphics.canvas.model.path.Arc
import `fun`.adaptive.graphics.canvas.model.path.MoveTo
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

/**
 * # Draw arcs with a path
 *
 * - Use the [path](fragment://) Canvas fragment to draw a path on a canvas.
 * - Use the [Arc](class://) path command to draw an arc.
 * - [Arc](class://) implements the SVG arc.
 * - The arc starts from the end point of the last command.
 *
 * **IMPORTANT** [Arc](class://) parameters are absolute.
 *
 * **IMPORTANT** [xAxisRotation](property://Arc) is in degrees
 */
@Adaptive
fun canvasPathArcExample(): AdaptiveFragment {
    
    canvas {
        size(240.dp) .. borders.outline .. backgrounds.surface

        path(
            listOf(
                MoveTo(37.5, 131.25),
                Arc(
                    rx = 75.0,
                    ry = 112.5,
                    xAxisRotation = 30.0,
                    largeArcFlag = 1,
                    sweepFlag = 0,
                    x2 = 150.0,
                    y2 = 18.75
                )
            )
        ) .. stroke(colors.onSurfaceFriendly)
    }

    return fragment()
}
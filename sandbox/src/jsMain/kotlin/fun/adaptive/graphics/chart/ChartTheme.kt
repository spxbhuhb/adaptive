package `fun`.adaptive.graphics.chart

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.canvas.api.stroke
import `fun`.adaptive.ui.theme.colors

class ChartTheme {

    val axisLabel = instructionsOf(
        fill(colors.onSurface)
    )

    val axisLine = instructionsOf(
        stroke(colors.onSurfaceVariant)
    )

    val axisGuide = instructionsOf(
        stroke(colors.onSurfaceVariant.opaque(0.2f))
    )
}
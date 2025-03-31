package `fun`.adaptive.chart.ui

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.canvas.api.stroke
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.normalFont
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.colors

class ChartTheme {

    val axisLabel = instructionsOf(
        fill(colors.onSurface),
        fontSize { 13.sp },
        normalFont
    )

    val axisLine = instructionsOf(
        stroke(colors.onSurfaceVariant)
    )

    val axisGuide = instructionsOf(
        stroke(colors.onSurfaceVariant.opaque(0.2f))
    )
}
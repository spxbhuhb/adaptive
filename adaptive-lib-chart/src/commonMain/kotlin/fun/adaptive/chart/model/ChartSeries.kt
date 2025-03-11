package `fun`.adaptive.chart.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.graphics.canvas.instruction.Translate
import `fun`.adaptive.ui.instruction.decoration.Color

@Adat
class ChartSeries(
    val offsetX: Double,
    val offsetY: Double,
    val color: Color,
    val points: List<ChartPoint>,
    val renderer: FragmentKey
)
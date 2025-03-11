package `fun`.adaptive.chart.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.ui.instruction.decoration.Color

@Adat
class ChartRenderSeries(
    val offsetX: Double,
    val offsetY: Double,
    val color: Color,
    val points: List<ChartRenderPoint>,
    val renderer: FragmentKey
)
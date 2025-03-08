package `fun`.adaptive.chart.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.ui.instruction.decoration.Color

@Adat
class ChartSeries(
    val color: Color,
    val points: List<ChartPoint>,
    val offset: Int,
    val size: Int,
    val renderer: FragmentKey? = null
)
package `fun`.adaptive.graphics.chart.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.ui.fragment.layout.RawPosition
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.layout.Orientation

@Adat
class ChartSeries(
    val color: Color,
    val points: List<ChartPoint>,
    val offset: Int,
    val size: Int,
    val renderer: FragmentKey? = null
)
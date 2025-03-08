package `fun`.adaptive.chart.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.ui.instruction.layout.Orientation

@Adat
class ChartAxis(
    val orientation: Orientation,
    val size: Double,
    val label: ChartLabel,
    val axisLine: Boolean,
    val ticks: List<ChartTick>,
    val labels: List<ChartLabel>,
    val guides: List<ChartGuide>,
    val renderer: FragmentKey? = null
)
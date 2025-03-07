package `fun`.adaptive.graphics.chart.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.ui.instruction.layout.Orientation

@Adat
class ChartAxis(
    val orientation: Orientation,
    val size: Double,
    val label: ChartLabel,
    val ticks: List<ChartTick>,
    val labels: List<ChartLabel>
)
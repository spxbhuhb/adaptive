package `fun`.adaptive.graphics.chart.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.ui.instruction.layout.Orientation

@Adat
class Axis(
    val orientation: Orientation,
    val size: Double,
    val label: Label,
    val ticks: List<Tick>,
    val labels: List<Label>
)
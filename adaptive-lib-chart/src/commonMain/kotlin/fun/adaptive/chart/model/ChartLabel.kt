package `fun`.adaptive.chart.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup

@Adat
class ChartLabel(
    val offset: Double,
    val text: String,
    val rotation: Double? = null,
    val instructions: AdaptiveInstructionGroup
)

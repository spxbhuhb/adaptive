package `fun`.adaptive.chart.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup

@Adat
class ChartTick(
    val offset: Double,
    val size: Double,
    val instructions: AdaptiveInstructionGroup? = null
)

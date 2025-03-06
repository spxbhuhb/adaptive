package `fun`.adaptive.graphics.chart.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup

@Adat
class Tick(
    val offset: Double,
    val size: Double,
    val instructions: AdaptiveInstructionGroup
)

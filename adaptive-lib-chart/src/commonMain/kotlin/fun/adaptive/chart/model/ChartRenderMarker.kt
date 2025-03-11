package `fun`.adaptive.chart.model

import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.emptyInstructions

data class ChartRenderMarker(
    val offset: Double,

    val tickSize : Double? = null,
    val tickInstructions: AdaptiveInstructionGroup = emptyInstructions,

    val labelText: String? = null,
    val labelRotation: Double? = null,
    val labelInstructions: AdaptiveInstructionGroup = emptyInstructions,

    val guide : Boolean = false,
    val guideInstructions: AdaptiveInstructionGroup = emptyInstructions
)
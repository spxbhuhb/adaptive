package `fun`.adaptive.grove.hydration.lfm

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.utility.UUID

@Adat
class LfmDescendant(
    val uuid: UUID<LfmDescendant>,
    val key: String,
    val mapping: List<LfmMapping>
) {
    val instructions: AdaptiveInstructionGroup
        get() = AdaptiveInstructionGroup(
            listOf(
                this.mapping.first { it.mapping.value is AdaptiveInstruction }.mapping.value as AdaptiveInstruction
            )
        )
}
package `fun`.adaptive.grove.hydration.lfm

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.utility.UUID

@Adat
class LfmDescendant(
    val uuid: UUID<LfmDescendant>,
    val key: FragmentKey,
    val mapping: List<LfmMapping>
) {
    constructor(key: String, instructions: AdaptiveInstructionGroup, vararg args: LfmMapping, uuid: UUID<LfmDescendant> = UUID()) :
        this(
            uuid,
            key,
            listOf(
                LfmMapping(
                    dependencyMask = 0,
                    LfmConst(
                        typeSignature<AdaptiveInstructionGroup>(),
                        instructions
                    )
                ),
                *args
            )
        )

    val instructions: AdaptiveInstructionGroup
        get() = AdaptiveInstructionGroup(
            listOf(
                this.mapping.first { it.mapping.value is AdaptiveInstruction }.mapping.value as AdaptiveInstruction
            )
        )

}
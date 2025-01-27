package `fun`.adaptive.grove.sheet

import `fun`.adaptive.auto.api.autoCollection
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.grove.api.hydrated
import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmExternalStateVariable
import `fun`.adaptive.grove.hydration.lfm.LfmFragment
import `fun`.adaptive.grove.ufd.UfdViewModel
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize

@Adaptive
fun sheet(viewModel: UfdViewModel) {
    val descendants = autoCollection(viewModel.fragments) ?: emptyList()

    box {
        maxSize .. SheetInner()

        for (item in descendants) {
            hydrated(item.model(), instructionsOf(DescendantInfo(item.uuid)))
        }
    }
}

private fun LfmDescendant.model() =
    LfmFragment(
        externalStateVariables,
        emptyList(),
        listOf(this)
    )

private val externalStateVariables =
    listOf(
        LfmExternalStateVariable(
            "instructions",
            AdaptiveInstructionGroup.typeSignature(),
            LfmConst(
                AdaptiveInstructionGroup.typeSignature(),
                emptyInstructions
            )
        )
    )

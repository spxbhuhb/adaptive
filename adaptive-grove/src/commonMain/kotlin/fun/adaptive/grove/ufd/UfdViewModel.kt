package `fun`.adaptive.grove.ufd

import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmMapping
import `fun`.adaptive.grove.sheet.SheetViewModel
import `fun`.adaptive.grove.sheet.SheetSelection
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.UIEvent
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.utility.UUID

class UfdViewModel : SheetViewModel() {
    val palette = autoCollectionOrigin(
        listOf(
            descendant("aui:text", emptyInstructions, LfmMapping(dependencyMask = 0, LfmConst("T", "Text"))),
            descendant("aui:box", instructionsOf(size(100.dp, 100.dp), border(colors.danger, 1.dp)))
        )
    )

    fun addDescendant(event: UIEvent) {
        val transfer = (event.transferData?.data as? LfmDescendant) ?: return
        val template = palette.firstOrNull { it.key == transfer.key } ?: return

        val templateInstructionMapping = template.mapping.first()
        val templateInstructions = templateInstructionMapping.mapping.value as AdaptiveInstructionGroup

        val instanceInstructions = templateInstructions.toMutableList() + Position(event.y.dp, event.x.dp)
        val instanceInstructionMapping =
            LfmMapping(
                dependencyMask = 0,
                mapping = LfmConst(
                    typeSignature<AdaptiveInstructionGroup>(),
                    AdaptiveInstructionGroup(instanceInstructions)
                )
            )

        val instanceMapping = listOf(instanceInstructionMapping) + template.mapping.drop(1)

        fragments += LfmDescendant(UUID(), template.key, instanceMapping)
    }

    fun clearSelection() {
        selection.update(emptySelection)
    }

    fun descendant(key: String, instructions: AdaptiveInstructionGroup, vararg args: LfmMapping) =
        LfmDescendant(
            UUID(),
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
}
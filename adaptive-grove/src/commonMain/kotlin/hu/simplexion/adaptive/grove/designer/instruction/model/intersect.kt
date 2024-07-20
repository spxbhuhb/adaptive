package hu.simplexion.adaptive.grove.designer.instruction.model

import hu.simplexion.adaptive.grove.designer.utility.Selection
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.instruction.Margin
import hu.simplexion.adaptive.ui.common.instruction.Padding
import hu.simplexion.adaptive.utility.firstOrNullIfInstance

fun intersect(selection: Selection): InstructionEditorData {

    val instructions = selection.items.last().instructions

    return InstructionEditorData(
        selection.containingFrame(selection) ?: Frame.NaF,
        instructions.firstOrNullIfInstance<Padding>() ?: Padding.NONE,
        instructions.firstOrNullIfInstance<Margin>() ?: Margin.NONE,
    )
}

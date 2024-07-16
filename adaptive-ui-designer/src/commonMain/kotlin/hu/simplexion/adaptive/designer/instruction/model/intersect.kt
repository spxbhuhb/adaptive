package hu.simplexion.adaptive.designer.instruction.model

import hu.simplexion.adaptive.designer.utility.Selection
import hu.simplexion.adaptive.ui.common.instruction.Margin
import hu.simplexion.adaptive.ui.common.instruction.Padding
import hu.simplexion.adaptive.utility.firstOrNullIfInstance

fun intersect(selection: Selection): InstructionEditorData {

    val instructions = selection.last().instructions

    return InstructionEditorData(
        instructions.firstOrNullIfInstance<Padding>() ?: Padding.NONE,
        instructions.firstOrNullIfInstance<Margin>() ?: Margin.NONE,
    )
}

package `fun`.adaptive.grove.designer.instruction.model

import `fun`.adaptive.grove.designer.utility.Selection
import `fun`.adaptive.ui.common.instruction.Frame
import `fun`.adaptive.ui.common.instruction.Margin
import `fun`.adaptive.ui.common.instruction.Padding
import `fun`.adaptive.utility.firstOrNullIfInstance

fun intersect(selection: Selection): InstructionEditorData {

    val instructions = selection.items.last().instructions

    return InstructionEditorData(
        selection.containingFrame(selection) ?: Frame.NaF,
        instructions.firstOrNullIfInstance<Padding>() ?: Padding.NONE,
        instructions.firstOrNullIfInstance<Margin>() ?: Margin.NONE,
    )
}

package `fun`.adaptive.grove.designer.instruction.model

import `fun`.adaptive.grove.designer.utility.Selection
import `fun`.adaptive.ui.instruction.layout.Frame
import `fun`.adaptive.ui.instruction.layout.Margin
import `fun`.adaptive.ui.instruction.layout.Padding

fun intersect(selection: Selection): InstructionEditorData {

    val instructions = selection.items.last().instructions

    return InstructionEditorData(
        selection.containingFrame(selection) ?: Frame.NaF,
        instructions.firstInstanceOfOrNull<Padding>() ?: Padding.NONE,
        instructions.firstInstanceOfOrNull<Margin>() ?: Margin.NONE,
    )
}

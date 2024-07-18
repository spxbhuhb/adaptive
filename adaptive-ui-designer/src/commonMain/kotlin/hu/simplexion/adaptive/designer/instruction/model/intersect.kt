package hu.simplexion.adaptive.designer.instruction.model

import hu.simplexion.adaptive.designer.overlay.model.containingFrame
import hu.simplexion.adaptive.designer.utility.Selection
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.instruction.Margin
import hu.simplexion.adaptive.ui.common.instruction.Padding
import hu.simplexion.adaptive.utility.firstOrNullIfInstance

fun intersect(selection: Selection): InstructionEditorData {

    val instructions = selection.last().instructions

    return InstructionEditorData(
        containingFrame(selection) ?: Frame.NaF,
        instructions.firstOrNullIfInstance<Padding>() ?: Padding.NONE,
        instructions.firstOrNullIfInstance<Margin>() ?: Margin.NONE,
    )
}

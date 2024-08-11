package `fun`.adaptive.grove.designer.instruction.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.ui.common.instruction.Frame
import `fun`.adaptive.ui.common.instruction.Margin
import `fun`.adaptive.ui.common.instruction.Padding

@Adat
class InstructionEditorData(
    val frame: Frame = Frame.NaF,
    val padding: Padding = Padding.NONE,
    val margin: Margin = Margin.NONE
) : AdatClass<InstructionEditorData>

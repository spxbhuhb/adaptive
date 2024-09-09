package `fun`.adaptive.grove.designer.instruction.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.ui.instruction.layout.Frame
import `fun`.adaptive.ui.instruction.layout.Margin
import `fun`.adaptive.ui.instruction.layout.Padding

@Adat
class InstructionEditorData(
    val frame: Frame = Frame.NaF,
    val padding: Padding = Padding.NONE,
    val margin: Margin = Margin.NONE
)

package hu.simplexion.adaptive.designer.instruction.model

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.ui.common.instruction.Margin
import hu.simplexion.adaptive.ui.common.instruction.Padding

@Adat
class InstructionEditorData(
    val padding: Padding = Padding.NONE,
    val margin: Margin = Margin.NONE
) : AdatClass<InstructionEditorData>

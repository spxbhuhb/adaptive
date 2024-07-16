package hu.simplexion.adaptive.designer.overlay.styles

import hu.simplexion.adaptive.foundation.instruction.instructionsOf
import hu.simplexion.adaptive.ui.common.instruction.border
import hu.simplexion.adaptive.ui.common.instruction.color
import hu.simplexion.adaptive.ui.common.instruction.dp

val containingBox = instructionsOf(
    border(color(0x0000ff), 1.dp)
)
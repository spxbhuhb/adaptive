package hu.simplexion.adaptive.grove.designer.overlay.styles

import hu.simplexion.adaptive.foundation.instruction.instructionsOf
import hu.simplexion.adaptive.ui.common.instruction.*

val guideColor = color(0xfc03d7)

val containingBox = instructionsOf(
    border(guideColor, 1.dp)
)

val guide = instructionsOf(
    borderTop(guideColor, 1.dp),
    borderLeft(guideColor, 1.dp)
)

val targetBox = instructionsOf(
    backgroundColor(color(0xa0a0a0))
)
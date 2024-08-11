package `fun`.adaptive.grove.designer.overlay.styles

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.common.instruction.*

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
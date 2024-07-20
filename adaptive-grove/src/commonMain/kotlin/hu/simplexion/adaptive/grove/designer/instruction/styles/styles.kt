package hu.simplexion.adaptive.grove.designer.instruction.styles

import hu.simplexion.adaptive.foundation.instruction.instructionsOf
import hu.simplexion.adaptive.ui.common.instruction.*

val instructionTitle = instructionsOf(
    backgroundColor(Color(0xe0e0e0u)), maxWidth, marginBottom { 8.dp }, paddingLeft { 8.dp }, cornerRadius(2.dp)
)

val instructionLabel = instructionsOf(
    fontSize(14.sp), smallCaps
)

val valueField = instructionsOf(
    AlignItems.startCenter,
    gapWidth(8.dp)
)

val valueLabel = instructionsOf(
    fontSize(12.sp), smallCaps
)

val dpEditorInstructions = instructionsOf(
    textColor(0x000000u),
    cornerRadius(2.dp),
    border(Color(0xa0a0a0u), 1.dp),
    height(24.dp),
    fontSize(12.sp),
    padding(left = 4.dp, right = 4.dp),
    width(48.dp)
)
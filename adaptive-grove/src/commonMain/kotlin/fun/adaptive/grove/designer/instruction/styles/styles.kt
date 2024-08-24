package `fun`.adaptive.grove.designer.instruction.styles

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.gapWidth
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.marginBottom
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.smallCaps
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.instruction.layout.AlignItems

val instructionTitle = instructionsOf(
    backgroundColor(0xe0e0e0u), maxWidth, marginBottom { 8.dp }, paddingLeft { 8.dp }, cornerRadius(2.dp)
)

val instructionLabel = instructionsOf(
    fontSize(14.sp), smallCaps
)

val valueField = instructionsOf(
    AlignItems.Companion.startCenter,
    gapWidth(8.dp)
)

val valueLabel = instructionsOf(
    fontSize(12.sp), smallCaps
)

val dpEditorInstructions = instructionsOf(
    textColor(0x000000u),
    cornerRadius(2.dp),
    border(color(0xa0a0a0u), 1.dp),
    height(24.dp),
    fontSize(12.sp),
    padding(left = 4.dp, right = 4.dp),
    width(48.dp)
)
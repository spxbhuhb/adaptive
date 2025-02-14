package `fun`.adaptive.ui.theme

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.instruction.sp

var textMedium = fontSize(14.sp)
var textSmall = fontSize(12.sp)

var whenNullInstructions = instructionsOf(textSmall, textColors.onSurfaceVariant)
var whenEmptyInstructions = instructionsOf(textSmall, textColors.onSurfaceVariant)
var whenValueInstructions = instructionsOf()

fun emptyInst(value: Any?) =
    when {
        value == null -> whenNullInstructions
        value is String && value.isEmpty() -> whenEmptyInstructions
        else -> whenValueInstructions
    }

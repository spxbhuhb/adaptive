package `fun`.adaptive.ui.common.instruction

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction

inline fun inputPlaceholder(valueFun: () -> String) = InputPlaceholder(valueFun())

class InputPlaceholder(
    val value: String
) : AdaptiveInstruction
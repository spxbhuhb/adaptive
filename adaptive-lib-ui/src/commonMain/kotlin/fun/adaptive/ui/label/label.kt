package `fun`.adaptive.ui.label

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textSmall

var smallLabelTheme = textColors.onSurfaceVariant .. textSmall

@Adaptive
fun label(vararg instructions: AdaptiveInstruction, label: () -> String): AdaptiveFragment {
    text(label(), textColors.onSurfaceVariant, instructions())
    return fragment()
}

@Adaptive
fun smallLabel(vararg instructions: AdaptiveInstruction, label: () -> String): AdaptiveFragment {
    text(label(), smallLabelTheme, instructions())
    return fragment()
}
package `fun`.adaptive.cookbook.shared

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.common.fragment.row
import `fun`.adaptive.ui.common.fragment.text
import `fun`.adaptive.ui.common.instruction.noSelect
import `fun`.adaptive.ui.common.instruction.textColor


@Adaptive
fun button(label: String, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    row(*button, *instructions) {
        text(label, textColor(white), textMedium, noSelect)
    }
    return fragment()
}
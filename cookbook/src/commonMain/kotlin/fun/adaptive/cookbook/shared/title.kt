package `fun`.adaptive.cookbook.shared

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.theme.textColors

@Adaptive
fun title(content: String?, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    box(instructions()) {
        maxSize
        text(content) .. letterSpacing(0.03) .. titleLarge .. semiBoldFont .. textColors.onSurface .. alignSelf.startCenter .. smallCaps
    }
    return fragment()
}
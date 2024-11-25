package `fun`.adaptive.cookbook.shared

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.letterSpacing
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.paddingTop
import `fun`.adaptive.ui.api.semiBoldFont
import `fun`.adaptive.ui.api.smallCaps
import `fun`.adaptive.ui.theme.textColors

@Adaptive
fun title(content: String?, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    box(*instructions) {
        maxSize
        text(content) .. letterSpacing(0.03) .. titleLarge .. semiBoldFont .. textColors.onSurface .. alignSelf.startCenter .. smallCaps
    }
    return fragment()
}
package `fun`.adaptive.ui.misc

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.semiBoldFont
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textMedium

@Adaptive
fun todo(data : Any? = null) : AdaptiveFragment {
    box {
        padding { 16.dp }
        text("TODO ${data ?: ""}") .. textColors.info .. textMedium .. semiBoldFont .. instructions()
    }
    return fragment()
}
package `fun`.adaptive.cookbook.shared

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.common.fragment.flowText
import `fun`.adaptive.ui.common.instruction.dp
import `fun`.adaptive.ui.common.instruction.lightFont
import `fun`.adaptive.ui.common.instruction.paddingTop

@Adaptive
fun subTitle(text: String) {
    flowText(text, *bodyMedium, lightFont, paddingTop(15.dp))
}
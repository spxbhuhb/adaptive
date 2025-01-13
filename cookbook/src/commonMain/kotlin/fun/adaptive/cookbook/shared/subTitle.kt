package `fun`.adaptive.cookbook.shared

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.flowText
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.api.lightFont
import `fun`.adaptive.ui.api.paddingTop

@Adaptive
fun subTitle(text: String) {
    flowText(text, bodyMedium, lightFont, paddingTop(15.dp))
}
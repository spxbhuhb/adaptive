package `fun`.adaptive.cookbook.shared

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.common.fragment.row
import `fun`.adaptive.ui.common.fragment.text
import `fun`.adaptive.ui.common.instruction.dp
import `fun`.adaptive.ui.common.instruction.height
import `fun`.adaptive.ui.common.instruction.paddingTop

@Adaptive
fun title(text: String) {
    row(height(213.dp), paddingTop(117.dp)) {
        text(text, titleLarge)
    }
}
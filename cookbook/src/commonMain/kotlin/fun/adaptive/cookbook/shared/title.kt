package `fun`.adaptive.cookbook.shared

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.paddingTop

@Adaptive
fun title(text: String) {
    row(height(213.dp), paddingTop(117.dp)) {
        text(text, titleLarge)
    }
}
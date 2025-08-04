package `fun`.adaptive.sandbox.support

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.dp


@Adaptive
fun example(
    text: String,
    _fixme_adaptive_content: @Adaptive () -> Unit
) {
    row {
        column {
            width { 300.dp }
            for (line in text.lines()) {
                text(line)
            }
        }
        box {
            _fixme_adaptive_content()
        }
    }
}
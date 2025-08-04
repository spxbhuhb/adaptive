package `fun`.adaptive.sandbox.support

import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.fillStrategy
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.instruction.dp


@Adaptive
fun hardCodedExample(
    markdown : String,
    content: @Adaptive () -> Unit
) {
    row {
        fillStrategy.constrain .. gap { 16.dp } .. maxWidth
        content()
        markdown(markdown)
    }
}
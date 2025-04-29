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
    @Adaptive
    _fixme_adaptive_content: () -> Unit
) {
    row {
        fillStrategy.constrain .. gap { 16.dp } .. maxWidth
        _fixme_adaptive_content()
        markdown(markdown)
    }
}
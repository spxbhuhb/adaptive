package `fun`.adaptive.sandbox.support

import `fun`.adaptive.document.ui.direct.h3
import `fun`.adaptive.document.ui.direct.markdownHint
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun examplePane(
    title: String,
    explanation : String? = null,
    @Adaptive
    _fixme_adaptive_content: () -> Unit
) {
    column {
        width { 600.dp }

        column {
            maxWidth .. borderBottom(colors.outline) .. marginBottom { 8.dp }
            h3(title)
        }

        if (explanation != null) {
            column {
                paddingBottom { 16.dp }
                markdownHint(explanation)
            }
        }

        column {
            padding { 23.dp } .. backgrounds.surfaceVariant .. cornerRadius { 4.dp }

            column {
                backgrounds.surface .. padding { 16.dp } .. cornerRadius { 4.dp }
                _fixme_adaptive_content()
            }
        }
    }
}
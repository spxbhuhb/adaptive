package `fun`.adaptive.sandbox.support

import `fun`.adaptive.document.ui.direct.h3
import `fun`.adaptive.document.ui.direct.markdownHint
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun examplePaneOld(
    title: String,
    explanation: String? = null,
    _fixme_adaptive_content: @Adaptive () -> Unit
) {
    column {
        width { 600.dp } .. borders.outline .. cornerRadius { 4.dp }

        column {
            maxWidth .. borderBottom(colors.outline)
            paddingHorizontal { 16.dp } .. paddingVertical { 4.dp }
            backgroundGradient(position(0.5.dp,0.dp), position(1.dp,1.dp), colors.surface, colors.infoSurface)
            cornerTopRadius { 4.dp }
            h3(title)
        }

        if (explanation != null) {
            column {
                padding { 16.dp } .. borderBottom(colors.outline)

                markdownHint(explanation)
            }
        }

        column {
            maxWidth .. padding { 24.dp } .. backgrounds.surfaceVariant .. cornerRadius { 4.dp }

            _fixme_adaptive_content()

        }
    }
}
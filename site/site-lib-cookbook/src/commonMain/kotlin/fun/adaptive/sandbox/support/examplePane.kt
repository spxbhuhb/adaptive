package `fun`.adaptive.sandbox.support

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.SizeStrategy
import `fun`.adaptive.ui.theme.backgrounds

@Adaptive
fun examplePane(
    title: String,
    @Adaptive
    _fixme_adaptive_content: () -> Unit
) {
    column {
        text(title) .. paddingBottom(4.dp)

        column {
            padding { 23.dp } .. backgrounds.surfaceVariant .. cornerRadius { 4.dp }

            column {
                backgrounds.surface .. padding { 16.dp } .. SizeStrategy(maxWidth = 600.dp) .. cornerRadius { 4.dp }
                _fixme_adaptive_content()
            }
        }
    }
}
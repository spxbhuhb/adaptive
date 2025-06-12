package `fun`.adaptive.sandbox.recipe.ui.input.button

import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.snackbar.infoNotification

@Adaptive
fun buttonNormalExample(): AdaptiveFragment {

    row {
        fillStrategy.constrain .. gap { 16.dp } .. maxWidth

        column {
            width { 240.dp }
            button("Normal") { infoNotification("Normal button clicked!") }
        }

        markdown(
            """
            * `button` variant
        """.trimIndent()
        )
    }

    return fragment()
}
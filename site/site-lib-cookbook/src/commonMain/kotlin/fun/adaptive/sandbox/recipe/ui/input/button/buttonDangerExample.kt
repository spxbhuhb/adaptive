package `fun`.adaptive.sandbox.recipe.ui.input.button

import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.button.dangerButton
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.snackbar.failNotification

@Adaptive
fun buttonDangerExample(): AdaptiveFragment {

    row {
        fillStrategy.constrain .. gap { 16.dp } .. maxWidth

        column {
            width { 240.dp }
            dangerButton("Danger") .. onClick { failNotification("Danger submit clicked!") }
        }

        markdown(
            """
            * `dangerButton` variant
        """.trimIndent()
        )
    }

    return fragment()
}
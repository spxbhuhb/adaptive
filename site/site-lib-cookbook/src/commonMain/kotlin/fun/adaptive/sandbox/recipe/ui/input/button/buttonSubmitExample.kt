package `fun`.adaptive.sandbox.recipe.ui.input.button

import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.input.button.submitButton
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.snackbar.infoNotification

@Adaptive
fun buttonSubmitExample(): AdaptiveFragment {

    row {
        fillStrategy.constrain .. gap { 16.dp } .. maxWidth

        column {
            width { 240.dp }
            submitButton("Submit") .. onClick { infoNotification("Submit clicked!") }
        }

        markdown(
            """
            * `submitButton` variant
        """.trimIndent()
        )
    }

    return fragment()
}
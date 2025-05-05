package `fun`.adaptive.sandbox.recipe.ui.input.button

import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.input.button.buttonInputBackend
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun buttonDisabledExample(): AdaptiveFragment {

    row {
        fillStrategy.constrain .. gap { 16.dp } .. maxWidth

        column {
            width { 240.dp }
            button("Disabled", viewBackend = buttonInputBackend { disabled = true })
        }

        markdown(
            """
            * `button` variant
            * backend.isDisabled == true
        """.trimIndent()
        )
    }

    return fragment()
}
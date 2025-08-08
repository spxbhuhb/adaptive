package `fun`.adaptive.doc.support

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.form.FormViewBackend
import `fun`.adaptive.ui.wrap.wrapFromTop

@Adaptive
fun configureForm(
    form: FormViewBackend,
    theme: ConfigureTheme = ConfigureTheme.default,
    content: @Adaptive () -> Unit
) : AdaptiveFragment {
    column(instructions()) {
        theme.formContainer

        wrapFromTop(
            theme.titleHeight,
            {
                row(theme.titleContainer) {
                    text("Configure") .. theme.titleText
                }
            }
        ) {
            localContext(form) {
                content()
            }
        }
    }

    return fragment()
}
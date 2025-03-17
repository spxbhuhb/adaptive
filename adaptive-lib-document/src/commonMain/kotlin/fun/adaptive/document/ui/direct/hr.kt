package `fun`.adaptive.document.ui.direct

import `fun`.adaptive.document.ui.DocumentTheme
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.box

@Adaptive
fun hr(theme : DocumentTheme = DocumentTheme.DEFAULT) : AdaptiveFragment {

    box { theme.rule } .. instructions()

    return fragment()
}
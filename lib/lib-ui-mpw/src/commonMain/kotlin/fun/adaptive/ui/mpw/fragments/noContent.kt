package `fun`.adaptive.ui.mpw.fragments

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.mpw.MultiPaneTheme
import `fun`.adaptive.ui.mpw.MultiPaneTheme.Companion.DEFAULT

@Adaptive
fun noContent(message: String, theme: MultiPaneTheme = DEFAULT) : AdaptiveFragment {
    box {
        theme.noContentContainer
        text(message) .. theme.noContentText
    }

    return fragment()
}
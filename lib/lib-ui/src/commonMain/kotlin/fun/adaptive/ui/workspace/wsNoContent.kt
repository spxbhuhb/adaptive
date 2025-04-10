package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.workspace.WorkspaceTheme.Companion.DEFAULT

@Adaptive
fun wsNoContent(message: String, theme: WorkspaceTheme = DEFAULT) : AdaptiveFragment {
    box {
        theme.noContentContainer
        text(message) .. theme.noContentText
    }

    return fragment()
}
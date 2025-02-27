package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.workspace.WorkspaceTheme.Companion.workspaceTheme

@Adaptive
fun wsNoContent(message: String, theme: WorkspaceTheme = workspaceTheme) {
    box {
        theme.noContentContainer
        text(message) .. theme.noContentText
    }
}
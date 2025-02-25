package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.workspace.WorkspaceTheme.Companion.workspaceTheme

@Adaptive
fun wsToolPane(
    pane: WorkspacePane,
    theme: WorkspaceTheme = workspaceTheme,
    @Adaptive
    _fixme_adaptive_content: () -> Unit
) {
    grid {
        theme.toolPaneContainer

        wsPaneTitle(pane, theme)

        box {
            theme.toolPaneContent
            _fixme_adaptive_content()
        }
    }
}
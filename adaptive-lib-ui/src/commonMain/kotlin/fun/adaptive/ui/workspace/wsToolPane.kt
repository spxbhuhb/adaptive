package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.hover
import `fun`.adaptive.ui.workspace.model.WorkspaceTheme.Companion.workspaceTheme
import `fun`.adaptive.ui.workspace.model.WorkspacePane
import `fun`.adaptive.ui.workspace.model.WorkspaceTheme

@Adaptive
fun wsToolPane(
    pane: WorkspacePane,
    theme: WorkspaceTheme = workspaceTheme,
    @Adaptive
    _fixme_adaptive_content: () -> Unit
) {
    val hover = hover()

    grid {
        theme.toolPaneContainer

        wsPaneTitle(pane, hover, theme)

        box {
            theme.toolPaneContent
            _fixme_adaptive_content()
        }
    }
}
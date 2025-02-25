package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.builtin.hide
import `fun`.adaptive.ui.builtin.remove
import `fun`.adaptive.ui.icon.actionIcon

@Adaptive
fun wsPaneTitle(
    pane: WorkspacePane,
    theme: WorkspaceTheme
) {
    row {
        theme.paneTitleContainer
        text(pane.name) .. theme.toolPaneTitleText
        row {
            actionIcon(Graphics.remove, tooltip = Strings.hide)
        }
    }
}
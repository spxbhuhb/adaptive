package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.findContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.builtin.hide
import `fun`.adaptive.ui.builtin.remove
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseIconTheme

@Adaptive
fun wsPaneTitle(
    pane: WorkspacePane,
    showActions : Boolean,
    theme: WorkspaceTheme
) {

    val workspace = fragment().findContext<Workspace>()!!

    row {
        theme.paneTitleContainer
        text(pane.name) .. theme.toolPaneTitleText
        row {
            if (showActions) {
                for (action in pane.actions) {
                    actionIcon(action.icon, tooltip = action.toolTip, theme = denseIconTheme) .. onClick {
                        action.action(workspace, pane)
                    }
                }
                actionIcon(Graphics.remove, tooltip = Strings.hide, theme = denseIconTheme) .. onClick {
                    workspace.toggle(pane)
                }
            }
        }
    }
}
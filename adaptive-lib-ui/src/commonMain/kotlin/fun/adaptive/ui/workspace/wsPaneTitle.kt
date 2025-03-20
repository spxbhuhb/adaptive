package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.findContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.hide
import `fun`.adaptive.ui.builtin.remove
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseIconTheme
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.menu.contextMenu
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneMenuAction

@Adaptive
fun wsPaneTitle(
    pane: WsPane<*, *>,
    showActions: Boolean,
    theme: WorkspaceTheme
) {

    val workspace = fragment().findContext<Workspace>() !!
    val actionContext = valueFrom { InputContext() }

    row {
        theme.paneTitleContainer

        text(pane.name) .. theme.toolPaneTitleText

        row {
            if (showActions || actionContext.popupOpen) {
                for (action in pane.actions) {
                    if (action is WsPaneMenuAction<*>) {
                        box {
                            actionIcon(action.icon, tooltip = action.tooltip, theme = denseIconTheme)
                            primaryPopup(actionContext) { hide ->
                                contextMenu(action.data, action.theme) { menuItem, modifiers ->
                                    action.selected(workspace, pane, menuItem, modifiers);
                                    hide()
                                }
                            }
                        }
                    } else {
                        actionIcon(action.icon, tooltip = action.tooltip, theme = denseIconTheme) .. onClick {
                            action.execute(workspace, pane)
                        }
                    }
                }
                actionIcon(Graphics.remove, tooltip = Strings.hide, theme = denseIconTheme) .. onClick {
                    workspace.toggle(pane)
                }
            }
        }
    }
}
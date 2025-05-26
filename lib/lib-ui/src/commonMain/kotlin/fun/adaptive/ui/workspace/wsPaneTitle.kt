package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.findContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.hide
import `fun`.adaptive.ui.generated.resources.remove
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseVariantIconTheme
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.menu.contextMenu
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneMenuAction

@Adaptive
fun wsPaneTitle(
    pane: WsPane<*>,
    showActions: Boolean,
    theme: WorkspaceTheme
) {

    val workspace = fragment().findContext<MultiPaneWorkspace>() !!
    val actionContext = valueFrom { InputContext() }

    row {
        theme.paneTitleContainer

        text(pane.name) .. theme.toolPaneTitleText

        row {
            theme.toolPaneTitleActionContainer

            if (showActions || actionContext.isPopupOpen) {
                for (action in pane.actions) {
                    if (action is WsPaneMenuAction<*>) {
                        box {
                            actionIcon(action.icon, tooltip = action.tooltip, theme = denseVariantIconTheme)
                            primaryPopup(actionContext) { hide ->
                                contextMenu(action.data, action.theme) { menuItem, modifiers ->
                                    action.selected(workspace, pane, menuItem, modifiers);
                                    hide()
                                }
                            }
                        }
                    } else {
                        actionIcon(action.icon, tooltip = action.tooltip, theme = denseVariantIconTheme) .. onClick {
                            action.execute(workspace, pane)
                        }
                    }
                }
                actionIcon(Graphics.remove, tooltip = Strings.hide, theme = denseVariantIconTheme) .. onClick {
                    workspace.toggle(pane)
                }
            }
        }
    }
}
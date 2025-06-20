package `fun`.adaptive.ui.mpw.fragments

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.hide
import `fun`.adaptive.ui.generated.resources.remove
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseVariantIconTheme
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.menu.contextMenu
import `fun`.adaptive.ui.mpw.MultiPaneTheme
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.ui.mpw.model.PaneMenuAction

@Adaptive
fun paneTitle(
    paneBackend : PaneViewBackend<*>,
    showActions: Boolean,
    theme: MultiPaneTheme
) {

    val pane = paneBackend.paneDef
    val actionContext = observe { InputContext() }

    row {
        theme.paneTitleContainer

        text(pane.name) .. theme.toolPaneTitleText

        row {
            theme.toolPaneTitleActionContainer

            if (showActions || actionContext.isPopupOpen) {
                for (action in paneBackend.getPaneActions()) {
                    if (action is PaneMenuAction<*>) {
                        box {
                            actionIcon(action.icon, tooltip = action.tooltip, theme = denseVariantIconTheme)
                            primaryPopup(actionContext) { hide ->
                                contextMenu(action.data, action.theme) { menuItem, modifiers ->
                                    action.selected(paneBackend.workspace, pane, menuItem, modifiers);
                                    hide()
                                }
                            }
                        }
                    } else {
                        actionIcon(action.icon, tooltip = action.tooltip, theme = denseVariantIconTheme) .. onClick {
                            action.execute()
                        }
                    }
                }
                actionIcon(Graphics.remove, tooltip = Strings.hide, theme = denseVariantIconTheme) .. onClick {
                    paneBackend.workspace.toggle(pane)
                }
            }
        }
    }
}
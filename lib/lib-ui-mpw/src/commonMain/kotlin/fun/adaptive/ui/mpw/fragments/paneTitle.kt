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
import `fun`.adaptive.ui.menu.menu
import `fun`.adaptive.ui.menu.menuBackend
import `fun`.adaptive.ui.menu.withContextMenu
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
                       paneMenuAction(paneBackend, action)
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

@Adaptive
private fun paneMenuAction(
    paneBackend : PaneViewBackend<*>,
    action : PaneMenuAction<*>,
) {
    val menuBackend = menuBackend(action.data) {
        action.selected(paneBackend.workspace, paneBackend.paneDef, it.item, it.modifiers)
        it.closeMenu()
    }

    withContextMenu(menuBackend) {
        actionIcon(action.icon, tooltip = action.tooltip, theme = denseVariantIconTheme)
    }
}
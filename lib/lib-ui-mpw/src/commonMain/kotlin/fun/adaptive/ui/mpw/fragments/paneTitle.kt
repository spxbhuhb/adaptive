package `fun`.adaptive.ui.mpw.fragments

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.Independent
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.generated.resources.hide
import `fun`.adaptive.ui.generated.resources.remove
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseVariantIconTheme
import `fun`.adaptive.ui.menu.menuBackend
import `fun`.adaptive.ui.menu.withPrimaryMenu
import `fun`.adaptive.ui.mpw.MultiPaneTheme
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.ui.mpw.model.PaneMenuAction

@Adaptive
fun paneTitle(
    paneBackend: PaneViewBackend<*>,
    showActions: Boolean,
    theme: MultiPaneTheme
) {

    val pane = paneBackend.paneDef

    var isPopupOpen = false

    adapter().traceWithContext = true

    row {
        theme.paneTitleContainer

        text(pane.name) .. theme.toolPaneTitleText

        row {
            theme.toolPaneTitleActionContainer

            if (showActions || isPopupOpen) {
                for (action in paneBackend.getPaneActions()) {
                    if (action is PaneMenuAction<*>) {
                        paneMenuAction(paneBackend, action) { isPopupOpen = it }
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
    paneBackend: PaneViewBackend<*>,
    action: PaneMenuAction<*>,
    onPopupOpenChanged: (Boolean) -> Unit
) {
    // FIXME tricky situation with menuBackend
    // The problem here is that the icon which opens the menu depends on hover.
    // We have to prevent hiding the icon when the menu is open even if it is not
    // hovered. But, as we call `onPopupChanged`, a new `menuBackend` is created
    // and the current popup state becomes lost.
    // Maybe a well-though out producer could solve this problem, but it requires
    // more in-depth investigation.
    @Independent
    val menuBackend = menuBackend(action.data) {
        action.selected(paneBackend.workspace, paneBackend.paneDef, it.item, it.modifiers)
    }.also {
        it.onPopupOpenChanged = onPopupOpenChanged
    }

    withPrimaryMenu(menuBackend) {
        actionIcon(action.icon, tooltip = action.tooltip, theme = denseVariantIconTheme)
    }
}
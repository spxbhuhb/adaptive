package `fun`.adaptive.ui.tab

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.close
import `fun`.adaptive.ui.generated.resources.more_vert
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseIconTheme
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.icon.smallCloseIconTheme
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.layout.GridTrack
import `fun`.adaptive.ui.menu.optContextMenu

typealias TabHandleFun = (model : TabContainer, tab : TabPane, activeTab : TabPane?, theme : TabTheme) -> Unit

@Adaptive
fun tabContainer(
    model : TabContainer,
    content : @Adaptive TabHandleFun, // handle render function
    theme : TabTheme = TabTheme.DEFAULT
) : AdaptiveFragment {

    val activeTab = model.tabs.firstOrNull { it.active }

    grid(instructions()) {
        theme.outerContainer

        header(model, theme, activeTab, content)

        if (activeTab != null) {
            localContext(activeTab.model) {
                actualize(activeTab.key, activeTab)
            }
        }
    }

    return fragment()
}

@Adaptive
fun tabHandle(
    model : TabContainer,
    tab : TabPane,
    activeTab : TabPane?,
    theme : TabTheme
) {
    val hover = hover()
    val active = (tab == activeTab)

    val containerStyle = if (active) {
        theme.activeTabHandleContainer
    } else {
        theme.tabHandleContainer
    }

    optContextMenu(tab.menu) {
        row {
            containerStyle

            if (tab.icon != null) {
                box {
                    theme.tabHandleIconContainer .. name("menu-icon")

                    icon(tab.icon) .. theme.tabHandleIcon
                }
            }

            row {
                text(tab.title) .. theme.tabHandleText

                if (tab.tooltip != null) {
                    hoverPopup(theme.tabHandleToolTip) {
                        text(tab.tooltip) .. theme.tabHandleToolTipText
                    }
                }
            }

            if (tab.closeable) {
                box {
                    theme.tabHandleCloseContainer .. name("close-icon")

                    if (active || hover) {
                        actionIcon(Graphics.close, tooltip = model.closeToolTip, theme = smallCloseIconTheme) ..
                            onClick {
                                it.stopPropagation()
                                model.closeFun(model, tab)
                            }
                    }
                }
            }
        }
    }
}

@Adaptive
private fun header(
    model : TabContainer,
    theme : TabTheme,
    activeTab : TabPane?,
    content : @Adaptive TabHandleFun
) {

    val hasMenu = model.menu.isNotEmpty()
    val actions = activeTab?.actions ?: emptyList()

    grid {
        theme.header
        headerColTemplate(actions.size, hasMenu, theme)

        row {
            theme.tabHandleList
            for (tab in model.tabs) {
                box {
                    onClick { model.switchFun(model, tab) }
                    content(model, tab, activeTab, theme)
                }
            }
        }

        if (actions.isNotEmpty()) {
            box { theme.separator }
            row {
                name("tab-container-actions")
                for (action in actions) {
                    actionIcon(action.icon, tooltip = action.tooltip, theme = denseIconTheme) .. onClick {
                        if (activeTab != null) action.action(activeTab)
                    }
                }
            }
        }

        if (hasMenu) {
            if (actions.isNotEmpty()) {
                box { theme.separator }
            }
            tabMenu(model)
        }
    }
}

@Adaptive
private fun tabMenu(model : TabContainer) {
    box {
        maxSize .. alignItems.center .. name("tab-menu")
        actionIcon(Graphics.more_vert, tooltip = model.menuToolTip, theme = denseIconTheme)
        //menuPopup(model.contextMenu)
    }
}

private fun headerColTemplate(actionCount : Int, hasMenu : Boolean, theme : TabTheme) : AdaptiveInstruction {

    val tracks = mutableListOf<GridTrack>(1.fr)

    if (actionCount > 0) {
        tracks.add(theme.tabActionSize * actionCount)
    }

    if (hasMenu) {
        if (actionCount > 0) {
            tracks.add(theme.separatorSize)
        }
        tracks.add(theme.contextMenuSize)
    }

    return colTemplate(*tracks.toTypedArray())
}


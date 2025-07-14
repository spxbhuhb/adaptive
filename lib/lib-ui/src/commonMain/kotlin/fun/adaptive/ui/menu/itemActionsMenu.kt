package `fun`.adaptive.ui.menu

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.actions
import `fun`.adaptive.ui.generated.resources.more_vert
import `fun`.adaptive.ui.icon.IconTheme
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.tableIconTheme

@Adaptive
fun <T> itemActionsMenu(
    menuItems : List<MenuItemBase<T>>,
    menuTheme : MenuTheme = MenuTheme.DEFAULT,
    iconTheme : IconTheme = tableIconTheme,
    selectedFun : MenuEventHandler<T>,
): AdaptiveFragment {

    val viewBackend = menuBackend(menuItems, menuTheme, selectedFun)

    box {
        actionIcon(Graphics.more_vert, Strings.actions, theme = iconTheme)
        primaryPopup(viewBackend) {
            column(viewBackend.theme.container, instructions()) {
                for (item in viewBackend.items) {
                    node(item, viewBackend)
                }
            }
        }
    }

    return fragment()
}
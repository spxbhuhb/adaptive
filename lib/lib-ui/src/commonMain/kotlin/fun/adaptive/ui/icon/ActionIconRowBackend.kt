package `fun`.adaptive.ui.icon

import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.ui.menu.MenuTheme
import `fun`.adaptive.ui.menu.menuBackend

class ActionIconRowBackend<T>(
    val priorityActions : List<MenuItemBase<T>> = emptyList(),
    val otherActions : List<MenuItemBase<T>> = emptyList(),
    val actionHandler : (MenuItem<T>) -> Unit
) {
    val menuBackend = menuBackend(otherActions, MenuTheme.DEFAULT) { event -> actionHandler(event.item) }

    fun isNotEmpty() = priorityActions.isNotEmpty() || otherActions.isNotEmpty()
}
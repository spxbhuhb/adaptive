package `fun`.adaptive.doc.example.menu

import `fun`.adaptive.doc.example.generated.resources.dining
import `fun`.adaptive.doc.example.generated.resources.lock
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.generated.resources.menu_book
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.MenuSeparator
import `fun`.adaptive.ui.menu.itemActionsMenu
import `fun`.adaptive.ui.snackbar.infoNotification

/**
 * # Item actions
 *
 * Intended to add actions to a list of items.
 *
 * - [itemActionsMenu](function://) shows an icon with a primary-click menu
 * - the menu backend is built automatically from the parameters
 * - [selectedFun](function://MenuViewBackend) is passed as parameter of [itemActionsMenu](function://)
 */
@Adaptive
fun menuItemActions(): AdaptiveFragment {

    val items = listOf(
        MenuItem(Graphics.lock, "Menu Item 1", "data-1"),
        MenuSeparator(),
        MenuItem(Graphics.menu_book, "Menu Item 2", "data-2"),
        MenuItem(
            Graphics.dining,
            "Menu Item with a long-long name, hopefully more than 200.dp",
            "data-3"
        )
    )

    itemActionsMenu(items) {
        infoNotification("Clicked on ${it.item.label} with modifiers ${it.modifiers}")
    }

    return fragment()
}


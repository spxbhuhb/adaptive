package `fun`.adaptive.sandbox.recipe.ui.menu

import `fun`.adaptive.cookbook.generated.resources.full_code
import `fun`.adaptive.cookbook.generated.resources.lock
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.generated.resources.content_copy
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.menu.*
import `fun`.adaptive.ui.snackbar.infoNotification

/**
 * # Basic menu
 *
 * - menus have items and separators
 * - use [inactive](property://MenuItem) to render a menu-item un-clickable
 * - use [shortcut](property://MenuItem) to display a keyboard shortcut (not handled by the menu)
 * - pass `null` in [icon](property://MenuItem) to have an item without an icon
 *
 * When a menu item is selected by the user, the menu calls [selectedFun](function://MenuViewBackend)
 *
 * [selectedFun](function://MenuViewBackend) gets a [MenuEvent](class://) that contains:
 * - the item clicked
 * - event modifiers
 * - the menu view backend
 *
 * When used directly, such as in this example, the menu occupies the available space, so here
 * we limit the size manually. When used with a popup, the menu automatically sizes the popup
 * to the longest item, so you don't have to set the size.
 */
@Adaptive
fun menuBasic(): AdaptiveFragment {

    val items = listOf<MenuItemBase<Any>>(
        MenuItem(Graphics.lock, "Menu Item 1", "data-1"),
        MenuItem(Graphics.full_code, "Inactive item", "data-2", inactive = true),
        MenuSeparator(),
        MenuItem(Graphics.content_copy, "Item with a shortcut", "data-5", shortcut = "⌘C"),
        MenuItem(null, "Item without an icon", "data-2")
    )

    val menuBackend = menuBackend(items) {
        infoNotification("Clicked on ${it.item.label} with modifiers ${it.modifiers}")
        it.closeMenu()
    }

    menu(menuBackend) .. width { 300.dp }

    return fragment()
}


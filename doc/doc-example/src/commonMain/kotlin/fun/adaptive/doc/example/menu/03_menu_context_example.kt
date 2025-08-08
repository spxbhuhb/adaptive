package `fun`.adaptive.doc.example.menu

import `fun`.adaptive.doc.example.generated.resources.dining
import `fun`.adaptive.doc.example.generated.resources.lock
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.generated.resources.menu_book
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.menu.*
import `fun`.adaptive.ui.snackbar.infoNotification

/**
 * # Context menu
 *
 * - [withContextMenu](function://) shows a menu on right-click
 * - [closeMenu](function://MenuEvent) of [MenuEvent](class://) closes the menu
 * - the menu sizes itself to the longest item, but it is at least 200.dp wide
 */
@Adaptive
fun menuContext(): AdaptiveFragment {

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

    val menuBackend = menuBackend(items) {
        infoNotification("Clicked on ${it.item.label} with modifiers ${it.modifiers}")
    }

    withContextMenu(menuBackend) {
        button("Right-click here for the menu!")
    }

    return fragment()
}


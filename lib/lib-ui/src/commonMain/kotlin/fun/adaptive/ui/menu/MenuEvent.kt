package `fun`.adaptive.ui.menu

import `fun`.adaptive.ui.instruction.event.EventModifier

class MenuEvent<T>(
    val menuViewBackend: MenuViewBackend<T>,
    val item: MenuItem<T>,
    val modifiers : Set<EventModifier>
) {

    fun closeMenu() {
        menuViewBackend.hidePopup?.invoke()
    }
}
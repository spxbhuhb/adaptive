package `fun`.adaptive.ui.menu

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.icon

typealias MenuEventHandler<T> = (event : MenuEvent<T>) -> Unit

fun <T> menuBackend(
    items: List<MenuItemBase<T>>,
    theme: MenuTheme = MenuTheme.DEFAULT,
    selectedFun: MenuEventHandler<T>
) = MenuViewBackend(
    items, theme, selectedFun
)

@Adaptive
fun <T> menu(
    viewBackend: MenuViewBackend<T>
): AdaptiveFragment {

    column(viewBackend.theme.container, instructions()) {
        for (item in viewBackend.items) {
            if (item.isVisible(fragment())) {
                node(item, viewBackend)
            }
        }
    }

    return fragment()
}

@Adaptive
internal fun <T> node(
    item: MenuItemBase<T>,
    viewBackend: MenuViewBackend<T>
) {
    val observed = observe { item }

    if (observed is MenuItem<T>) {
        label(observed, viewBackend)
    } else {
        box { viewBackend.theme.separator }
    }

    // TODO sub menus for context menu
}

@Adaptive
private fun <T> label(
    item: MenuItem<T>,
    viewBackend: MenuViewBackend<T>
) {
    val hover = hover()
    val theme = viewBackend.theme

    val foreground = theme.itemForeground(hover, item.inactive)

    val icon = item.icon

    row(theme.item, theme.itemBackground(hover, item.inactive)) {

        onClick {
            it.stopPropagation()
            if (item.inactive) return@onClick
            viewBackend.selectedFun(MenuEvent(viewBackend, item, it.modifiers))
            if (viewBackend.autoClose) {
                viewBackend.hidePopup?.invoke()
            }
        }

        row {
            if (icon != null) {
                icon(icon, theme.icon) .. foreground
            } else {
                box { theme.icon }
            }
            text(item.label) .. theme.label .. foreground
        }

        if (item.shortcut != null) {
            row {
                text(item.shortcut) .. theme.shortcut .. theme.itemForeground(hover, true)
            }
        }
    }
}
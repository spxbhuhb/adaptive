package `fun`.adaptive.ui.menu

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.event.EventModifier

typealias MenuItemSelectedFun<T> = (item: MenuItem<T>, modifiers: Set<EventModifier>) -> Unit

@Adaptive
fun <T> contextMenu(
    items: List<MenuItem<T>>,
    theme: ContextMenuTheme = ContextMenuTheme.DEFAULT,
    selectedFun: MenuItemSelectedFun<T>,
): AdaptiveFragment {

    column(theme.container, instructions()) {
        for (item in items) {
            column {
                node(item, selectedFun, theme)
            }
        }
    }

    return fragment()
}

@Adaptive
private fun <T> node(
    item: MenuItem<T>,
    selectedFun: MenuItemSelectedFun<T>,
    theme: ContextMenuTheme
) {
    val observed = valueFrom { item }

    label(observed, selectedFun, theme)

    // TODO sub menus for context menu
}

@Adaptive
private fun <T> label(
    item: MenuItem<T>,
    selectedFun: MenuItemSelectedFun<T>,
    theme: ContextMenuTheme
) {
    val hover = hover()

    val foreground = theme.itemForeground(hover, false)

    val icon = item.icon

    row(theme.item, theme.itemBackground(hover)) {
        spaceBetween

        onClick {
            selectedFun(item, it.modifiers)
            it.stopPropagation()
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
package `fun`.adaptive.ui.menu

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.icon

@Adaptive
fun <T> contextMenu(
    items: List<MenuItem<T>>,
    theme: ContextMenuTheme = ContextMenuTheme.DEFAULT,
): AdaptiveFragment {

    column(theme.container, instructions()) {
        for (item in items) {
            column {
                node(item, theme)
            }
        }
    }

    return fragment()
}

@Adaptive
private fun <T> node(
    item: MenuItem<T>,
    theme: ContextMenuTheme
) {
    val observed = valueFrom { item }

    label(observed, theme)

    // TODO sub menus for context menu
}

@Adaptive
private fun <T> label(
    item: MenuItem<T>,
    theme: ContextMenuTheme
) {
    val hover = hover()
    val colors = theme.itemColors(false, hover)
    val variantItemColors = theme.variantItemColors(false, hover)

    val icon = item.icon

    row(theme.item, colors) {
        spaceBetween

        onClick {
            item.onClick(item, it.modifiers)
        }

        row {
            if (icon != null) {
                icon(icon, theme.icon) .. colors
            } else {
                box { theme.icon }
            }
            text(item.label) .. theme.label .. colors
        }

        if (item.shortcut != null) {
            row {
                text(item.shortcut) .. theme.shortcut .. variantItemColors
            }
        }
    }
}
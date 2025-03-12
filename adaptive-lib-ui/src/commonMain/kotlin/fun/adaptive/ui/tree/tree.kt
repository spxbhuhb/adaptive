package `fun`.adaptive.ui.tree

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.arrow_drop_down
import `fun`.adaptive.ui.builtin.arrow_right
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun <T> tree(
    items: List<TreeItem<T>>,
    theme: TreeTheme = TreeTheme.DEFAULT,
    @Adaptive
    _KT_74337_context_menu: ((item: TreeItem<T>) -> Unit)? = null
): AdaptiveFragment {

    column(theme.container, instructions()) {
        for (item in items) {
            column {
                node(item, theme, 0.dp, _KT_74337_context_menu)
            }
        }
    }

    return fragment()
}

@Adaptive
private fun <T> node(
    item: TreeItem<T>,
    theme: TreeTheme,
    offset: DPixel,
    @Adaptive
    _KT_74337_context_menu: ((item: TreeItem<T>) -> Unit)?,
) {
    val observed = valueFrom { item }

    label(observed, theme, offset, observed.open, _KT_74337_context_menu) { item.open = ! item.open }

    if (observed.open) {
        column {
            for (child in observed.children) {
                column {
                    node(child, theme, offset + theme.indent, _KT_74337_context_menu)
                }
            }
        }
    }
}

@Adaptive
private fun <T> label(
    item: TreeItem<T>,
    theme: TreeTheme,
    offset: DPixel,
    open: Boolean,
    @Adaptive
    _KT_74337_context_menu: ((item: TreeItem<T>) -> Unit)?,
    toggle: () -> Unit,
) {
    val hover = hover()
    val colors = theme.itemColors(false, hover)

    row(theme.item, colors) {
        paddingLeft { offset }

        onClick {
            item.onClick(item, it.modifiers)
            toggle()
        }

        onDoubleClick {
            item.onClick(item, it.modifiers)
            toggle()
        }

        box {
            size(24.dp, 24.dp)
            when {
                item.children.isEmpty() -> box { }
                open -> svg(Graphics.arrow_drop_down)
                else -> svg(Graphics.arrow_right)
            }
        }

        icon(item.icon, theme.icon) .. colors

        text(item.title) .. theme.label .. colors

        if (_KT_74337_context_menu != null) {
            contextPopup {
                popupAlign.belowCenter
                _KT_74337_context_menu(item)
            }
        }
    }
}
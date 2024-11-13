package `fun`.adaptive.ui.tree

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.hover
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.builtin.Res
import `fun`.adaptive.ui.builtin.arrow_drop_down
import `fun`.adaptive.ui.builtin.arrow_right
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.tree.theme.TreeTheme
import `fun`.adaptive.ui.tree.theme.treeTheme

@Adaptive
fun tree(
    items: List<TreeItem>,
    theme: TreeTheme = treeTheme,
    vararg instructions: AdaptiveInstruction,
): AdaptiveFragment {

    column(*instructions, *theme.container) {
        for (item in items) {
            column {
                node(item, theme, theme.indent)
            }
        }
    }

    return fragment()
}

@Adaptive
private fun node(
    item: TreeItem,
    theme: TreeTheme,
    offset: DPixel,
) {
    var open = false

    label(item, theme, offset, open) { open = ! open }

    column {
        if (open) {
            for (child in item.children) {
                column {
                    node(child, theme, offset + theme.indent)
                }
            }
        }
    }
}

@Adaptive
private fun label(
    item: TreeItem,
    theme: TreeTheme,
    offset: DPixel,
    open: Boolean,
    toggle: () -> Unit,
) {
    val hover = hover()
    val colors = theme.itemColors(false, hover)

    row(*theme.item, *colors) {
        onClick { toggle() }
        alignItems.startCenter .. height { theme.itemHeight }
        paddingLeft { offset } .. width { theme.width }

        box {
            size(24.dp, 24.dp)
            when {
                item.children.isEmpty() -> box { }
                open -> svg(Res.drawable.arrow_drop_down)
                else -> svg(Res.drawable.arrow_right)
            }
        }
        text(item.title) .. theme.label .. colors
    }
}
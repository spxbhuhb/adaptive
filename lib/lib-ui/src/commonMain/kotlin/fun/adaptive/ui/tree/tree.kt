package `fun`.adaptive.ui.tree

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.instruction.event.UIEvent

typealias TreeItemSelectedFun<IT, CT> = (viewModel: TreeViewModel<IT, CT>, item: TreeItem<IT>, modifiers: Set<EventModifier>) -> Unit
typealias TreeKeyDownFun<IT, CT> = (viewModel: TreeViewModel<IT, CT>, item: TreeItem<IT>, event: UIEvent) -> Unit
typealias TreeContextMenuBuilder<IT, CT> = ((hide: () -> Unit, viewModel: TreeViewModel<IT, CT>, item: TreeItem<IT>) -> Unit)

@Adaptive
fun <IT, CT> tree(
    viewModel: TreeViewModel<IT, CT>,
    @Adaptive
    _KT_74337_contextMenuBuilder: TreeContextMenuBuilder<IT, CT>? = null
): AdaptiveFragment {

    column(viewModel.theme.container, instructions()) {
        for (item in viewModel.items) {
            column {
                node(viewModel, item, 0.dp, _KT_74337_contextMenuBuilder)
            }
        }
    }

    return fragment()
}

@Adaptive
private fun <IT, CT> node(
    viewModel: TreeViewModel<IT, CT>,
    item: TreeItem<IT>,
    offset: DPixel,
    @Adaptive
    _KT_74337_contextMenuBuilder: TreeContextMenuBuilder<IT, CT>?
) {
    val observed = valueFrom { item }

    label(viewModel, item, offset, _KT_74337_contextMenuBuilder)

    if (observed.open) {
        column {
            for (child in observed.children) {
                column {
                    node(viewModel, child, offset + viewModel.theme.indent, _KT_74337_contextMenuBuilder)
                }
            }
        }
    }
}

@Adaptive
private fun <IT, CT> label(
    viewModel: TreeViewModel<IT, CT>,
    item: TreeItem<IT>,
    offset: DPixel,
    @Adaptive
    _KT_74337_contextMenuBuilder: TreeContextMenuBuilder<IT, CT>?
) {
    val observed = valueFrom { item }
    val theme = viewModel.theme

    val foreground = theme.itemForeground(observed.selected, true)

    row(theme.item, theme.itemBackground(observed.selected, true)) {
        paddingLeft { offset }

        onClick {
            viewModel.selectedFun?.invoke(viewModel, observed, it.modifiers)
            if (viewModel.openWithSingleClick) {
                observed.open = ! observed.open
            }
        }

        onDoubleClick {
            viewModel.selectedFun?.invoke(viewModel, observed, it.modifiers)
            observed.open = ! observed.open
        }

        onKeydown {
            viewModel.keyDownFun?.invoke(viewModel, observed, it)
        }

        box {
            theme.handleContainer
            // This is tricky, `onClick` on `row` will run if this runs. If both reverses open,
            // it will remain the same at the end.
            onClick { if (! viewModel.openWithSingleClick) observed.open = ! observed.open }

            when {
                observed.children.isEmpty() -> box { }
                observed.open -> svg(theme.handleIconOpen) .. foreground .. theme.handleIcon
                else -> svg(theme.handleIconClosed) .. foreground .. theme.handleIcon
            }
        }

        icon(observed.icon, theme.icon) .. foreground

        text(observed.title) .. theme.label .. foreground

        if (_KT_74337_contextMenuBuilder != null) {
            contextPopup { hide ->
                popupAlign.belowCenter
                _KT_74337_contextMenuBuilder(hide, viewModel, observed)
            }
        }
    }
}
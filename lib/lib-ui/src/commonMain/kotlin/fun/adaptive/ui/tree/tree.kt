package `fun`.adaptive.ui.tree

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.instruction.event.UIEvent

typealias TreeItemSelectedFun<IT, CT> = (backend: TreeViewBackend<IT, CT>, item: TreeItem<IT>, modifiers: Set<EventModifier>) -> Unit
typealias TreeKeyDownFun<IT, CT> = (backend: TreeViewBackend<IT, CT>, item: TreeItem<IT>, event: UIEvent) -> Unit
typealias TreeContextMenuBuilder<IT, CT> = ((hide: () -> Unit, backend: TreeViewBackend<IT, CT>, item: TreeItem<IT>) -> Unit)

@Adaptive
fun <IT, CT> tree(
    viewBackend: TreeViewBackend<IT, CT>,
    @Adaptive
    _KT_74337_contextMenuBuilder: TreeContextMenuBuilder<IT, CT>? = null
): AdaptiveFragment {

    val observed = valueFrom { viewBackend }

    column(observed.theme.container, instructions()) {
        onKeydown { observed.onKeyDown(it) }

        for (item in observed.topItems) {
            node(observed, item, 0.dp, _KT_74337_contextMenuBuilder)
        }
    }

    return fragment()
}

@Adaptive
private fun <IT, CT> node(
    viewBackend: TreeViewBackend<IT, CT>,
    item: TreeItem<IT>,
    offset: DPixel,
    @Adaptive
    _KT_74337_contextMenuBuilder: TreeContextMenuBuilder<IT, CT>?
) {
    val observed = valueFrom { item }

    column {

        localContext(observed) {
            label(viewBackend, item, offset, _KT_74337_contextMenuBuilder)
        }

        if (observed.open) {
            column {
                for (child in observed.children) {
                    column {
                        node(viewBackend, child, offset + viewBackend.theme.indent, _KT_74337_contextMenuBuilder)
                    }
                }
            }
        }
    }
}

@Adaptive
private fun <IT, CT> label(
    viewBackend: TreeViewBackend<IT, CT>,
    item: TreeItem<IT>,
    offset: DPixel,
    @Adaptive
    _KT_74337_contextMenuBuilder: TreeContextMenuBuilder<IT, CT>?
) {
    val observed = valueFrom { item }
    val theme = viewBackend.theme

    val foreground = theme.itemForeground(observed.selected, true)

    row(theme.item, theme.itemBackground(observed.selected, true)) {
        paddingLeft { offset }

        onClick {
            viewBackend.selectedFun?.invoke(viewBackend, observed, it.modifiers)
            if (viewBackend.singleClickOpen) {
                observed.open = ! observed.open
            }
        }

        onDoubleClick {
            viewBackend.selectedFun?.invoke(viewBackend, observed, it.modifiers)
            observed.open = ! observed.open
        }

        box {
            theme.handleContainer
            // This is tricky, `onClick` on `row` will run if this runs. If both reverses open,
            // it will remain the same at the end.
            onClick { if (! viewBackend.singleClickOpen) observed.open = ! observed.open }

            when {
                observed.children.isEmpty() -> box { }
                observed.open -> icon(theme.handleIconOpen) .. foreground .. theme.handleIcon
                else -> icon(theme.handleIconClosed) .. foreground .. theme.handleIcon
            }
        }

        if (observed.icon != null) {
            icon(observed.icon !!, theme.icon) .. foreground
        }

        text(observed.title) .. theme.label .. foreground

        if (_KT_74337_contextMenuBuilder != null) {
            contextPopup { hide ->
                popupAlign.belowCenter
                _KT_74337_contextMenuBuilder(hide, viewBackend, observed)
            }
        }
    }
}
package `fun`.adaptive.ui.tree

import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.instruction.event.Keys
import `fun`.adaptive.ui.instruction.event.UIEvent
import kotlin.properties.Delegates.observable

/**
 * Backend for a tree view component that manages tree data structure and selection state.
 *
 * @param IT The type of data stored in tree items
 * @param CT The type of context object used by the tree
 */
class TreeViewBackend<IT, CT>(
    items: List<TreeItem<IT>>,
    selection: List<TreeItem<IT>> = emptyList(),
    val context: CT,
    val selectedFun: TreeItemSelectedFun<IT, CT>? = null,
    val keyDownFun: TreeKeyDownFun<IT, CT>? = null,
    val theme: TreeTheme = TreeTheme.DEFAULT,
    val multiSelect: Boolean = false,
    val singleClickOpen: Boolean = false,
    val doubleClickOpen: Boolean = true
) : SelfObservable<TreeViewBackend<IT, CT>>() {

    var items by observable(items, ::notify)
    var selection = selection

    /**
     * Transforms the tree backend into a new tree backend with different item and context types.
     *
     * @param NIT The new item type
     * @param NCT The new context type
     * @param context New context object
     * @param selectedFun New selection callback function
     * @param keyDownFun New key down callback function
     * @param transform Function to transform each tree item from original to new type
     *
     * @return New TreeViewBackend instance with transformed items and context
     */
    fun <NIT, NCT> transform(
        context: NCT,
        selectedFun: TreeItemSelectedFun<NIT, NCT>? = null,
        keyDownFun: TreeKeyDownFun<NIT, NCT>? = null,
        transform: (original: TreeItem<IT>, newParent: TreeItem<NIT>?) -> TreeItem<NIT>
    ) =
        TreeViewBackend(
            items.map { transform(it, null) },
            emptyList(),
            context,
            selectedFun,
            keyDownFun,
            theme,
            multiSelect,
            singleClickOpen
        )

    fun expandAll() = items.forEach { it.expandAll() }

    fun collapseAll() = items.forEach { it.collapseAll() }

    fun defaultSelectedFun(item: TreeItem<IT>, modifiers: Set<EventModifier>) {
        when {
            multiSelect && modifiers.any { it.isMultiSelect } -> {
                if (item !in selection) {
                    selection += item
                    item.selected = true
                } else {
                    selection -= item
                    item.selected = false
                }
            }

            else -> {
                selection.forEach { it.selected = false }
                selection = listOf(item)
                item.selected = true
            }
        }
    }

    fun defaultKeyDownFun(item: TreeItem<IT>, event: UIEvent) {
        if (multiSelect) return // FIXME moving in the tree with multiselect

        when (event.keyInfo?.key) {
            Keys.ARROW_UP -> item.previous()
            Keys.ARROW_DOWN -> item.next()
            else -> null
        }?.also { newItem ->
            selectedFun?.invoke(this, newItem, event.modifiers)
        }
    }

    fun onKeyDown(event: UIEvent) {
        val item = selection.firstOrNull() ?: return
        defaultKeyDownFun(item, event)
        event.stopPropagation()
        event.preventDefault()
    }

    companion object {

        fun <IT, CT> defaultSelectedFun(
            viewBackend: TreeViewBackend<IT, CT>, item: TreeItem<IT>, modifiers: Set<EventModifier>
        ) {
            viewBackend.defaultSelectedFun(item, modifiers)
        }

        fun <IT, CT> defaultKeyDownFun(
            viewBackend: TreeViewBackend<IT, CT>, item: TreeItem<IT>, event: UIEvent
        ) {
            viewBackend.defaultKeyDownFun(item, event)
        }

    }
}
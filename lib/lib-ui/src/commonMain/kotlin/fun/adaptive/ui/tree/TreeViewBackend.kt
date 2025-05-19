package `fun`.adaptive.ui.tree

import `fun`.adaptive.foundation.api.findContext
import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.ui.instruction.event.EventModifier
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
    var selection by observable(selection, ::notify)

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
    fun <NIT,NCT> transform(
        context: NCT,
        selectedFun: TreeItemSelectedFun<NIT, NCT>? = null,
        keyDownFun: TreeKeyDownFun<NIT, NCT>? = null,
        transform: (original : TreeItem<IT>, newParent : TreeItem<NIT>?) -> TreeItem<NIT>
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

    private fun select(event : UIEvent) : TreeItem<IT>? {
        val fragment = event.fragmentsByPosition().lastOrNull() ?: return null
        val item = fragment.findContext<TreeItem<IT>>() ?: return null

        selectedFun?.invoke(this, item.value, event.modifiers)

        return item
    }

    fun onClick(event: UIEvent) {
       val item = select(event) ?: return
       if (singleClickOpen) {
            item.toggle()
       }
    }

    fun onDoubleClick(event: UIEvent) {
        val item = select(event) ?: return
        if (doubleClickOpen) {
            item.toggle()
        }
    }

    fun onKeydown(event: UIEvent) {

    }

    companion object {

        fun <IT, CT> defaultSelectedFun(
            viewModel: TreeViewBackend<IT, CT>, item: TreeItem<IT>, modifiers: Set<EventModifier>
        ) {
            when {
                viewModel.multiSelect && modifiers.any { it.isMultiSelect } -> {
                    if (item !in viewModel.selection) {
                        viewModel.selection += item
                        item.selected = true
                    } else {
                        viewModel.selection -= item
                        item.selected = false
                    }
                }

                else -> {
                    viewModel.selection.forEach { it.selected = false }
                    viewModel.selection = listOf(item)
                    item.selected = true
                }
            }
        }

        fun <IT, CT> defaultKeyDownFun(
            viewModel: TreeViewBackend<IT, CT>, item: TreeItem<IT>, event: UIEvent
        ) {
            val safeKeyInfo = event.keyInfo ?: return

            when (safeKeyInfo.key) {
                "ArrowUp" -> {
                    TODO()
                }
            }
        }

    }
}
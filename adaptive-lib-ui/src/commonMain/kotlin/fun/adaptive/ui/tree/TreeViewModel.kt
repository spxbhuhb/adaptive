package `fun`.adaptive.ui.tree

import `fun`.adaptive.general.Observable
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.instruction.event.UIEvent
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

class TreeViewModel<T>(
    items: List<TreeItem<T>>,
    selection: List<TreeItem<T>> = emptyList(),
    val selectedFun: TreeItemSelectedFun<T>? = null,
    val keyDownFun: TreeKeyDownFun<T>? = null,
    val theme: TreeTheme = TreeTheme.DEFAULT,
    val multiSelect: Boolean = false,
    val openWithSingleClick: Boolean = false
) : Observable<TreeViewModel<T>>() {

    var items by observable(items, ::notify)
    var selection by observable(selection, ::notify)

    @Suppress("unused")
    fun <VT> notify(property: KProperty<*>, oldValue: VT, newValue: VT) {
        notifyListeners()
    }

    override var value: TreeViewModel<T>
        get() = this
        set(_) {
            throw UnsupportedOperationException()
        }

    companion object {

        fun <T> defaultSelectedFun(
            viewModel: TreeViewModel<T>, item: TreeItem<T>, modifiers: Set<EventModifier>
        ) {
            when {
                viewModel.multiSelect && EventModifier.META in modifiers -> {
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

        fun <T> defaultKeyDownFun(
            viewModel: TreeViewModel<T>, item: TreeItem<T>, event: UIEvent
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
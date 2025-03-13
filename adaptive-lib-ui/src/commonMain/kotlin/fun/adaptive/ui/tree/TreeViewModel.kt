package `fun`.adaptive.ui.tree

import `fun`.adaptive.general.Observable
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.instruction.event.UIEvent
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

class TreeViewModel<IT, CT>(
    items: List<TreeItem<IT>>,
    selection: List<TreeItem<IT>> = emptyList(),
    val context: CT,
    val selectedFun: TreeItemSelectedFun<IT, CT>? = null,
    val keyDownFun: TreeKeyDownFun<IT, CT>? = null,
    val theme: TreeTheme = TreeTheme.DEFAULT,
    val multiSelect: Boolean = false,
    val openWithSingleClick: Boolean = false
) : Observable<TreeViewModel<IT, CT>>() {

    var items by observable(items, ::notify)
    var selection by observable(selection, ::notify)

    @Suppress("unused")
    fun <VT> notify(property: KProperty<*>, oldValue: VT, newValue: VT) {
        notifyListeners()
    }

    override var value: TreeViewModel<IT, CT>
        get() = this
        set(_) {
            throw UnsupportedOperationException()
        }

    companion object {

        fun <IT, CT> defaultSelectedFun(
            viewModel: TreeViewModel<IT, CT>, item: TreeItem<IT>, modifiers: Set<EventModifier>
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

        fun <IT, CT> defaultKeyDownFun(
            viewModel: TreeViewModel<IT, CT>, item: TreeItem<IT>, event: UIEvent
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
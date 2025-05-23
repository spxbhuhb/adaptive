package `fun`.adaptive.ui.value

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.LifecycleBound
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewBackend
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.model.AvTreeSetup
import kotlin.reflect.KClass

class AvUiTreeViewBackend<SPEC : Any>(
    backend: BackendAdapter,
    specClass: KClass<SPEC>,
    treeSetup: AvTreeSetup,
    selectedFun: (backend: AvUiTreeViewBackend<SPEC>, TreeItem<AvValue<SPEC>>, Set<EventModifier>) -> Unit
) : LifecycleBound {

    @Suppress("UNCHECKED_CAST")
    val treeBackend = TreeViewBackend<AvValue<SPEC>, AvUiTreeViewBackend<SPEC>>(
        emptyList(),
        selectedFun = { b, item, modifiers ->
            selectedFun(this, item, modifiers)
            TreeViewBackend.defaultSelectedFun(b, item, modifiers)
        },
        multiSelect = false,
        context = this
    )

    val avTree = AvUiTree(
        backend,
        specClass,
        treeSetup
    )

    init {
        avTree.addListener { treeBackend.items = it }
    }

    fun expandAll() {
        treeBackend.items.forEach { it.expandAll() }
    }

    fun collapseAll() {
        treeBackend.items.forEach { it.collapseAll() }
    }

    override fun dispose(fragment: AdaptiveFragment, index: Int) {
        avTree.stop()
    }
}
package `fun`.adaptive.ui.value

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.LifecycleBound
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewBackend
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.model.AvTreeDef
import kotlin.reflect.KClass

class AvUiTreeSupport<SPEC : Any>(
    backend: BackendAdapter,
    specClass: KClass<SPEC>,
    treeDef: AvTreeDef,
    selectedFun: (backend: AvUiTreeSupport<SPEC>, TreeItem<AvValue<SPEC>>, Set<EventModifier>) -> Unit,
    sortNodesFun: (List<TreeItem<AvValue<SPEC>>>) -> List<TreeItem<AvValue<SPEC>>> = { it },
    titleFun: (AvValue<SPEC>) -> String = { it.nameLike }
) : LifecycleBound {

    @Suppress("UNCHECKED_CAST")
    val treeBackend = TreeViewBackend<AvValue<SPEC>, AvUiTreeSupport<SPEC>>(
        emptyList(),
        selectedFun = { b, item, modifiers ->
            selectedFun(this, item, modifiers)
            TreeViewBackend.defaultSelectedFun(b, item, modifiers)
        },
        multiSelect = false,
        context = this
    )

    val treeSubscriber = AvUiTreeSubscriber(
        backend,
        specClass,
        treeDef,
        sortNodesFun = sortNodesFun,
        titleFun = titleFun
    )

    init {
        treeSubscriber.addListener {
            treeBackend.topItems = sortNodesFun(it)
        }
    }

    fun expandAll() {
        treeBackend.topItems.forEach { it.expandAll() }
    }

    fun collapseAll() {
        treeBackend.topItems.forEach { it.collapseAll() }
    }

    fun dispose() {
        treeSubscriber.stop()
    }

    override fun dispose(fragment: AdaptiveFragment, index: Int) {
        dispose()
    }

    fun flatPathAndValueList() =
        treeSubscriber.flatPathAndValueList()

}
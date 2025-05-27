package `fun`.adaptive.grove.doc.ws.browser

import `fun`.adaptive.grove.doc.model.GroveDocValue
import `fun`.adaptive.grove.doc.model.avDomain
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.value.AvUiTreeViewBackend
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.value.AvValue

abstract class AbstractDocToolViewBackend<VB : AbstractDocToolViewBackend<VB>>(
    override val workspace: MultiPaneWorkspace
) : PaneViewBackend<VB>() {

    val tree = AvUiTreeViewBackend(workspace.backend, String::class, avDomain.treeDef, ::selectedFun)

//    override fun accepts(pane: WsPaneType<Unit>, modifiers: Set<EventModifier>, item: NamedItem): Boolean {
//        return true
//    }
//
//    override fun load(pane: WsPaneType<AvValue<String>>, modifiers: Set<EventModifier>, item: NamedItem): WsPaneType<AvValue<String>> {
//        val spaceItem = item.asAvValue<AioSpaceSpec>()
//        return pane.copy(
//            name = item.name,
//            data = spaceItem.asAvValue(),
//            icon = iconFor(spaceItem)
//        )
//    }

    fun expandAll() {
        tree.treeBackend.expandAll()
    }

    fun collapseAll() {
        tree.treeBackend.collapseAll()
    }

    abstract fun selectedFun(backend: AvUiTreeViewBackend<String>, item: TreeItem<AvValue<String>>, modifiers: Set<EventModifier>)

    fun docPathNames(item : GroveDocValue): List<String> {
        return tree.treeSubscriber.pathNames(item)
    }

}
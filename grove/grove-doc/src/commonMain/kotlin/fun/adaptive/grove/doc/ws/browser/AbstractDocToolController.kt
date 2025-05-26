package `fun`.adaptive.grove.doc.ws.browser

import `fun`.adaptive.grove.doc.model.avDomain
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.value.AvUiTreeViewBackend
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType
import `fun`.adaptive.value.AvValue

abstract class AbstractDocToolController(
    override val workspace: MultiPaneWorkspace
) : WsPaneController<AbstractDocToolController>() {

    val viewBackend = AvUiTreeViewBackend(workspace.backend, String::class, avDomain.treeDef, ::selectedFun)

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
        viewBackend.treeBackend.expandAll()
    }

    fun collapseAll() {
        viewBackend.treeBackend.collapseAll()
    }

    abstract fun selectedFun(backend: AvUiTreeViewBackend<String>, item: TreeItem<AvValue<String>>, modifiers: Set<EventModifier>)

}
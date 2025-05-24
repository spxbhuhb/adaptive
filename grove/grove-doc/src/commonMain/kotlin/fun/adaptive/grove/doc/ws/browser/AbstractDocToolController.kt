package `fun`.adaptive.grove.doc.ws.browser

import `fun`.adaptive.grove.doc.model.docTreeSetup
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.value.AvUiTreeViewBackend
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.value.AvValue

abstract class AbstractDocToolController(
    override val workspace: MultiPaneWorkspace
) : WsPaneController<Unit>() {

    val viewBackend = AvUiTreeViewBackend(workspace.backend, String::class, docTreeSetup, ::selectedFun)

    fun expandAll() {
        viewBackend.treeBackend.expandAll()
    }

    fun collapseAll() {
        viewBackend.treeBackend.collapseAll()
    }

    abstract fun selectedFun(backend: AvUiTreeViewBackend<String>, item: TreeItem<AvValue<String>>, modifiers: Set<EventModifier>)

}
package `fun`.adaptive.document.ws.browser

import `fun`.adaptive.document.value.DocMarkers
import `fun`.adaptive.document.ws.DocTreeModel
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.workspace.WithWorkspace
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.ui.AvUiTree

abstract class AbstractDocToolController(
    override val workspace: Workspace
) : WsPaneController<Unit>(), WithWorkspace {

    val treeViewModel = TreeViewModel<AvValueId, AbstractDocToolController>(
        emptyList(),
        selectedFun = ::selectedFun,
        multiSelect = false,
        context = this
    )

    val valueTreeStore = AvUiTree(
        backend,
        transport,
        scope,
        DocMarkers.DOCUMENT,
        DocMarkers.SUB_DOCUMENTS,
        DocMarkers.TOP_DOCUMENTS,
        ::refreshTop
    )

    fun start() {
        valueTreeStore.start()
    }

    fun refreshTop(tops: List<TreeItem<AvValueId>>) {
        treeViewModel.items = tops
    }

    fun expandAll() {
        treeViewModel.items.forEach { it.expandAll() }
    }

    fun collapseAll() {
        treeViewModel.items.forEach { it.collapseAll() }
    }

    abstract fun selectedFun(viewModel: DocTreeModel, treeItem: TreeItem<AvValueId>, modifiers: Set<EventModifier>)

}
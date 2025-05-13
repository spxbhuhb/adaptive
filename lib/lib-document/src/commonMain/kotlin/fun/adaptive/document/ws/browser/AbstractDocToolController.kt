package `fun`.adaptive.document.ws.browser

import `fun`.adaptive.document.api.DocApi
import `fun`.adaptive.document.value.DocMarkers
import `fun`.adaptive.document.value.DocRefLabels
import `fun`.adaptive.document.ws.DocTreeModel
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.value.AvUiTree
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.avByMarker

abstract class AbstractDocToolController(
    override val workspace: Workspace
) : WsPaneController<Unit>() {

    val service = getService<DocApi>(workspace.transport)

    val treeViewModel = TreeViewModel<AvValueId, AbstractDocToolController>(
        emptyList(),
        selectedFun = ::selectedFun,
        multiSelect = false,
        context = this
    )

    val valueTreeStore = AvUiTree(
        workspace.backend,
        Any::class,
        DocRefLabels.DOCUMENT_PARENT,
        avByMarker(DocMarkers.DOCUMENT)
    )

    fun start() {
        valueTreeStore.addListener { treeViewModel.items = it }
    }

    fun expandAll() {
        treeViewModel.items.forEach { it.expandAll() }
    }

    fun collapseAll() {
        treeViewModel.items.forEach { it.collapseAll() }
    }

    abstract fun selectedFun(viewModel: DocTreeModel, treeItem: TreeItem<AvValueId>, modifiers: Set<EventModifier>)

}
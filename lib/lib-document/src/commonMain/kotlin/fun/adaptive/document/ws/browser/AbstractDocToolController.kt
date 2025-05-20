package `fun`.adaptive.document.ws.browser

import `fun`.adaptive.document.api.DocApi
import `fun`.adaptive.document.value.DocMarkers
import `fun`.adaptive.document.value.DocRefLabels
import `fun`.adaptive.document.ws.DocTreeModel
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewBackend
import `fun`.adaptive.ui.value.AvUiTree
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.model.AvTreeSetup

abstract class AbstractDocToolController(
    override val workspace: MultiPaneWorkspace
) : WsPaneController<Unit>() {

    val service = getService<DocApi>(workspace.transport)

    val treeViewBackend = TreeViewBackend<AvValue<Any>, AbstractDocToolController>(
        emptyList(),
        selectedFun = ::selectedFun,
        multiSelect = false,
        context = this
    )

    val treeSetup = AvTreeSetup(
        nodeMarker = DocMarkers.DOCUMENT,
        childListMarker = DocMarkers.SUB_DOCUMENTS,
        rootListMarker = DocMarkers.TOP_DOCUMENTS,
        parentRefLabel = DocRefLabels.DOCUMENT_PARENT,
        childListRefLabel = DocRefLabels.CHILD_DOCUMENTS
    )

    val valueTreeStore = AvUiTree(
        workspace.backend,
        Any::class,
        treeSetup
    )

    fun start() {
        valueTreeStore.addListener { treeViewBackend.items = it }
    }

    fun expandAll() {
        treeViewBackend.items.forEach { it.expandAll() }
    }

    fun collapseAll() {
        treeViewBackend.items.forEach { it.collapseAll() }
    }

    abstract fun selectedFun(viewModel: DocTreeModel, treeItem: TreeItem<AvValue<Any>>, modifiers: Set<EventModifier>)

}
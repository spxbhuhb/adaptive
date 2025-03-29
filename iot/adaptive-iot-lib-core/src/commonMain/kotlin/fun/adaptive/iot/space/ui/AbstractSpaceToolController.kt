package `fun`.adaptive.iot.space.ui

import `fun`.adaptive.iot.space.AioSpaceApi
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.ui.value.AvUiTree
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.workspace.WithWorkspace
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController

abstract class AbstractSpaceToolController(
    override val workspace: Workspace
) : WsPaneController<Unit>(), WithWorkspace {

    val spaceService = getService<AioSpaceApi>(transport)

    val treeViewModel = TreeViewModel<AvValueId, AbstractSpaceToolController>(
        emptyList(),
        selectedFun = ::selectedFun,
        multiSelect = false,
        context = this
    )

    val valueTreeStore = AvUiTree(
        backend,
        transport,
        scope,
        SpaceMarkers.SPACE,
        SpaceMarkers.SUB_SPACES,
        SpaceMarkers.TOP_SPACES,
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

    abstract fun selectedFun(viewModel: SpaceTreeModel, treeItem: TreeItem<AvValueId>, modifiers: Set<EventModifier>)

}
package `fun`.adaptive.iot.space.ui.model

import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.iot.space.AioSpaceApi
import `fun`.adaptive.iot.space.markers.SpaceMarkers
import `fun`.adaptive.iot.space.ui.SpaceTreeModel
import `fun`.adaptive.iot.ui.AioUiTree
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.workspace.WithWorkspace
import `fun`.adaptive.ui.workspace.Workspace

class SpaceToolState(
    override val workspace: Workspace,
    val config: SpaceToolConfig
) : SelfObservable<SpaceToolState>(), WithWorkspace {

    val spaceService = getService<AioSpaceApi>(transport)

    val treeViewModel = TreeViewModel<AioValueId, SpaceToolState>(
        emptyList(),
        selectedFun = ::selectedFun,
        multiSelect = false,
        context = this
    )

    val valueTreeStore = AioUiTree(
        backend,
        transport,
        scope,
        SpaceMarkers.SPACE,
        SpaceMarkers.SUB_SPACES,
        SpaceMarkers.TOP_SPACES,
        ::refreshTop
    )

    fun refreshTop(tops : List<TreeItem<AioValueId>>) {
        treeViewModel.items = tops
    }

    fun expandAll() {
        treeViewModel.items.forEach { it.expandAll() }
    }

    fun collapseAll() {
        treeViewModel.items.forEach { it.collapseAll() }
    }

    fun selectedFun(viewModel: SpaceTreeModel, treeItem: TreeItem<AioValueId>, modifiers: Set<EventModifier>) {
        val item = valueTreeStore[treeItem.data] ?: return
        workspace.addContent(item, modifiers)
        TreeViewModel.defaultSelectedFun(viewModel, treeItem, modifiers)
    }

    fun start() {
        valueTreeStore.start()
    }

}
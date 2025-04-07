package `fun`.adaptive.iot.space.ui

import `fun`.adaptive.iot.space.AioSpaceApi
import `fun`.adaptive.iot.space.AioSpaceSpec
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.value.AvUiTree
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.value.AvValueId

abstract class AbstractSpaceToolController(
    override val workspace: Workspace
) : WsPaneController<Unit>() {

    val spaceService = getService<AioSpaceApi>(transport)

    val treeViewModel = TreeViewModel<AvValueId, AbstractSpaceToolController>(
        emptyList(),
        selectedFun = ::selectedFun,
        multiSelect = false,
        context = this
    )

    val valueTreeStore = AvUiTree(
        spaceService,
        backend,
        AioSpaceSpec::class,
        SpaceMarkers.TOP_SPACES,
        SpaceMarkers.SUB_SPACES
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

    abstract fun selectedFun(viewModel: SpaceTreeModel, treeItem: TreeItem<AvValueId>, modifiers: Set<EventModifier>)

}
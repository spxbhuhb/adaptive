package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.workspace.WithWorkspace
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.ui.AvNameCache
import `fun`.adaptive.value.ui.AvUiTree

class HistoryToolController(
    override val workspace: Workspace
) : WsPaneController<Unit>(), WithWorkspace {

    val hisNames = AvNameCache(
        workspace.backend,
        workspace.transport,
        workspace.scope,
        PointMarkers.HIS
    ).also {
        it.start()
    }
//
//    val treeViewModel = TreeViewModel<AvValueId, HistoryToolController>(
//        emptyList(),
//        selectedFun = ::selectedFun,
//        multiSelect = false,
//        context = this
//    )
//
//    val valueTreeStore = AvUiTree(
//        backend,
//        transport,
//        scope,
//        DeviceMarkers.DEVICE,
//        DeviceMarkers.SUB_DEVICES,
//        DeviceMarkers.TOP_DEVICES,
//        ::refreshTop
//    )
//
//    fun start() {
//        hisNames.start()
//    }
//
//    fun refreshTop(tops: List<TreeItem<AvValueId>>) {
//        treeViewModel.items = tops
//    }
//
//    fun expandAll() {
//        treeViewModel.items.forEach { it.expandAll() }
//    }
//
//    fun collapseAll() {
//        treeViewModel.items.forEach { it.collapseAll() }
//    }
//
//    fun selectedFun(viewModel: HistoryTreeModel, treeItem: TreeItem<AvValueId>, modifiers: Set<EventModifier>) {
//
//    }

}
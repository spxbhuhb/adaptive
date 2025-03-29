package `fun`.adaptive.iot.device.ui

import `fun`.adaptive.iot.device.AioDeviceApi
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.ui.value.AvUiTree
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.workspace.WithWorkspace
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController

abstract class AbstractDeviceToolController(
    override val workspace: Workspace
) : WsPaneController<Unit>(), WithWorkspace {

    val deviceService = getService<AioDeviceApi>(transport)

    val treeViewModel = TreeViewModel<AvValueId, AbstractDeviceToolController>(
        emptyList(),
        selectedFun = ::selectedFun,
        multiSelect = false,
        context = this
    )

    val valueTreeStore = AvUiTree(
        backend,
        transport,
        scope,
        DeviceMarkers.DEVICE,
        DeviceMarkers.SUB_DEVICES,
        DeviceMarkers.TOP_DEVICES,
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

    abstract fun selectedFun(viewModel: DeviceTreeModel, treeItem: TreeItem<AvValueId>, modifiers: Set<EventModifier>)

}
package `fun`.adaptive.iot.device.ui

import `fun`.adaptive.iot.device.AioDeviceApi
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.device.publish.AioDeviceTreePublishApi
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.value.AvUiTree
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.value.AvValueId

abstract class AbstractDeviceToolController(
    override val workspace: Workspace
) : WsPaneController<Unit>() {

    val deviceService = getService<AioDeviceApi>(transport)

    val treeViewModel = TreeViewModel<AvValueId, AbstractDeviceToolController>(
        emptyList(),
        selectedFun = ::selectedFun,
        multiSelect = false,
        context = this
    )

    val valueTreeStore = AvUiTree(
        getService<AioDeviceTreePublishApi>(transport),
        backend,
        AioDeviceSpec::class,
        DeviceMarkers.TOP_DEVICES,
        DeviceMarkers.SUB_DEVICES
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

    abstract fun selectedFun(viewModel: DeviceTreeModel, treeItem: TreeItem<AvValueId>, modifiers: Set<EventModifier>)

}
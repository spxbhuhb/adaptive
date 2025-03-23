package `fun`.adaptive.iot.device.ui.editor

import `fun`.adaptive.adaptive_lib_iot.generated.resources.humidity
import `fun`.adaptive.adaptive_lib_iot.generated.resources.temperature
import `fun`.adaptive.iot.device.ui.AbstractDeviceToolController
import `fun`.adaptive.iot.device.ui.DeviceTreeModel
import `fun`.adaptive.iot.haystack.PhScienceMarkers
import `fun`.adaptive.iot.point.AioPointApi
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.iot.point.computed.AioComputedPointSpec
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.workspace.Workspace

class DeviceEditorToolController(
    workspace: Workspace
) : AbstractDeviceToolController(workspace) {

    val pointService = getService<AioPointApi>(transport)

    override fun selectedFun(viewModel: DeviceTreeModel, treeItem: TreeItem<AvValueId>, modifiers: Set<EventModifier>) {
        val item = valueTreeStore[treeItem.data] ?: return
        workspace.addContent(item, modifiers)
        TreeViewModel.defaultSelectedFun(viewModel, treeItem, modifiers)
    }

    fun addDevice(name: String, marker: String, data: AvValueId?, virtual: Boolean) {
        io {
            val deviceId = deviceService.add(name, marker, data, virtual)

            if (virtual) {
                pointService.add(
                    Strings.temperature,
                    PointMarkers.SIM_POINT,
                    deviceId,
                    AioComputedPointSpec(dependencyMarker = PhScienceMarkers.TEMP)
                )

                pointService.add(
                    Strings.humidity,
                    PointMarkers.SIM_POINT,
                    deviceId,
                    AioComputedPointSpec(dependencyMarker = PhScienceMarkers.HUMIDITY)
                )
            }
        }
    }

    fun moveUp(item: AvValueId) {
        io { deviceService.moveUp(item) }
    }

    fun moveDown(item: AvValueId) {
        io { deviceService.moveDown(item) }
    }

}
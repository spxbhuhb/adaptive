package `fun`.adaptive.iot.device.ui.editor

import `fun`.adaptive.iot.device.AioDeviceApi
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.generated.resources.renameFail
import `fun`.adaptive.iot.generated.resources.renameSuccess
import `fun`.adaptive.iot.generated.resources.saveFail
import `fun`.adaptive.iot.generated.resources.saveSuccess
import `fun`.adaptive.iot.space.AioSpaceApi
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.value.iconFor
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem

class DeviceEditorContentController(
    override val workspace: Workspace
) : WsPaneController<AvItem<AioDeviceSpec>>() {

    val spaceService = getService<AioSpaceApi>(transport)
    val deviceService = getService<AioDeviceApi>(transport)

    override fun accepts(pane: WsPaneType<AvItem<AioDeviceSpec>>, modifiers: Set<EventModifier>, item: NamedItem): Boolean {
        return (item is AvItem<*>) && (DeviceMarkers.DEVICE in item.markers)
    }

    override fun load(pane: WsPaneType<AvItem<AioDeviceSpec>>, modifiers: Set<EventModifier>, item: NamedItem): WsPaneType<AvItem<AioDeviceSpec>> {
        val deviceItem = item.asAvItem<AioDeviceSpec>()
        return pane.copy(
            name = item.name,
            data = deviceItem,
            icon = iconFor(deviceItem)
        )
    }

    fun rename(deviceId: AvValueId, name: String) {
        remote(Strings.renameSuccess, Strings.renameFail) {
            deviceService.rename(deviceId, name)
        }
    }

    fun setSpec(deviceId: AvValueId, spec: AioDeviceSpec) {
        remote(Strings.saveSuccess, Strings.saveFail) {
            deviceService.setSpec(deviceId, spec)
        }
    }

    fun setSpace(deviceId : AvValueId, spaceId: AvValueId) {
        remote(Strings.saveSuccess, Strings.saveFail) {
            spaceService.setSpace(deviceId, spaceId, SpaceMarkers.SPACE_DEVICES)
        }
    }

}
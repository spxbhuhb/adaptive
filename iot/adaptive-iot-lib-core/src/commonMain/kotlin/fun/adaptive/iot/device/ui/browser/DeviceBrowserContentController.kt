package `fun`.adaptive.iot.device.ui.browser

import `fun`.adaptive.iot.device.AioDeviceApi
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.WithWorkspace
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType
import `fun`.adaptive.model.NamedItem

class DeviceBrowserContentController(
    override val workspace: Workspace
) : WsPaneController<DeviceBrowserWsItem>(), WithWorkspace {

    val deviceService = getService<AioDeviceApi>(transport)

    override fun accepts(pane: WsPaneType<DeviceBrowserWsItem>, modifiers: Set<EventModifier>, item: NamedItem): Boolean {
        return (item is DeviceBrowserWsItem)
    }

    override fun load(pane: WsPaneType<DeviceBrowserWsItem>, modifiers: Set<EventModifier>, item: NamedItem): WsPaneType<DeviceBrowserWsItem> {
        return pane.copy(
            name = item.name,
            data = item as DeviceBrowserWsItem,
            icon = item.config.icon
        )
    }

}
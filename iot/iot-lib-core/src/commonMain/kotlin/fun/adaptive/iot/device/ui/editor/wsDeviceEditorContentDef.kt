package `fun`.adaptive.iot.device.ui.editor

import `fun`.adaptive.iot.app.IotWsModule
import `fun`.adaptive.iot.app.WsItemTypes
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValue.Companion.asAvItem

fun wsDeviceEditorContentDef(
    module: IotWsModule<*>
) {
    val workspace = module.workspace

    workspace.addContentPaneBuilder(WsItemTypes.WSIT_DEVICE) { item ->
        WsPane(
            UUID(),
            workspace = workspace,
            item.name,
            workspace.getItemConfig(item.type).icon,
            WsPanePosition.Center,
            module.WSPANE_DEVICE_CONTENT,
            controller = DeviceEditorContentController(workspace),
            data = item.asAvItem()
        )
    }
}

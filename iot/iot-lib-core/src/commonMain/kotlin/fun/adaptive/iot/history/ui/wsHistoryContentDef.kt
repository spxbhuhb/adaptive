package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.iot.app.IotWsModule
import `fun`.adaptive.iot.device.ui.DeviceItems
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID


fun wsHistoryContentDef(
    module: IotWsModule<*>
) {
    val workspace = module.workspace

    workspace.addContentPaneBuilder(DeviceItems.WSIT_HISTORY) { item ->
        WsPane(
            UUID(),
            workspace = workspace,
            item.name,
            workspace.getItemConfig(item.type).icon,
            WsPanePosition.Center,
            module.WSPANE_HISTORY_CONTENT,
            controller = HistoryContentController(workspace),
            data = item as HistoryBrowserWsItem
        )
    }
}

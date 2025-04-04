package `fun`.adaptive.iot.domain.rht.ui

import `fun`.adaptive.iot.app.IotWsModule
import `fun`.adaptive.iot.domain.rht.AmvRelativeHumidityAndTemperature
import `fun`.adaptive.iot.space.ui.browser.SpaceBrowserContentController
import `fun`.adaptive.iot.space.ui.browser.SpaceBrowserWsItem
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun wsRhtBrowserContentDef(
    module: IotWsModule<*>
) {
    val workspace = module.workspace

    workspace.addContentPaneBuilder(AmvRelativeHumidityAndTemperature.WSIT_RHT_BROWSER_ITEM) { item ->
        WsPane(
            UUID(),
            workspace = workspace,
            item.name,
            workspace.getItemConfig(item.type).icon,
            WsPanePosition.Center,
            module.WSPANE_RHT_BROWSER_CONTENT,
            controller = SpaceBrowserContentController(workspace),
            data = item as SpaceBrowserWsItem
        )
    }
}
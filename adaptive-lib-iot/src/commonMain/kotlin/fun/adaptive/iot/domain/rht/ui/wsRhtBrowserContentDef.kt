package `fun`.adaptive.iot.domain.rht.ui

import `fun`.adaptive.iot.domain.rht.AmvRelativeHumidityAndTemperature
import `fun`.adaptive.iot.space.ui.browser.SpaceBrowserContentController
import `fun`.adaptive.iot.space.ui.browser.SpaceBrowserWsItem
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun wsRhtBrowserContentDef(context: AioWsContext) {
    val workspace = context.workspace

    workspace.addContentPaneBuilder(AmvRelativeHumidityAndTemperature.WSIT_RHT_BROWSER_ITEM) { item ->
        WsPane(
            UUID(),
            item.name,
            context[item].icon,
            WsPanePosition.Center,
            AioWsContext.WSPANE_RHT_BROWSER_CONTENT,
            controller = SpaceBrowserContentController(workspace),
            data = item as SpaceBrowserWsItem
        )
    }
}
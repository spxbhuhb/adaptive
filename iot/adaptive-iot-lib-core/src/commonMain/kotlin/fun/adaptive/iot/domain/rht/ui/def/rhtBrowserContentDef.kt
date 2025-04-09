package `fun`.adaptive.iot.domain.rht.ui.def

import `fun`.adaptive.iot.app.IotWsModule
import `fun`.adaptive.iot.domain.rht.ui.controller.RhtWsItem
import `fun`.adaptive.iot.domain.rht.ui.controller.RhtBrowserContentController
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun rhtBrowserContentDef(
    module: IotWsModule<*>
) {
    val workspace = module.workspace

    workspace.addContentPaneBuilder(RhtWsItem.Companion.WSIT_RHT_ITEM) { item ->
        WsPane(
            UUID(),
            workspace = workspace,
            item.name,
            workspace.getItemConfig(item.type).icon,
            WsPanePosition.Center,
            module.WSPANE_RHT_BROWSER_CONTENT,
            controller = RhtBrowserContentController(workspace),
            data = item as RhtWsItem
        )
    }
}
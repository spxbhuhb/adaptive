package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem


fun wsHistoryContentDef(context: AioWsContext) {
    val workspace = context.workspace

    workspace.addContentPaneBuilder(AioWsContext.WSIT_HISTORY) { item ->
        WsPane(
            UUID(),
            workspace = workspace,
            item.name,
            context[item].icon,
            WsPanePosition.Center,
            AioWsContext.WSPANE_HISTORY_CONTENT,
            controller = HistoryContentController(workspace),
            data = item as HistoryBrowserWsItem
        )
    }
}

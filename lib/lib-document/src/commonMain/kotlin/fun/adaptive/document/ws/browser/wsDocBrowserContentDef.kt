package `fun`.adaptive.document.ws.browser

import `fun`.adaptive.document.app.DocWsModule
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun wsDocBrowserContentDef(module: DocWsModule<*>) {
    val workspace = module.workspace

    workspace.addContentPaneBuilder(module.WSIT_DOC_ITEM) { item ->
        WsPane(
            UUID(),
            workspace,
            item.name,
            workspace.getItemConfig(item.type).icon,
            WsPanePosition.Center,
            module.WSPANE_DOC_BROWSER_CONTENT,
            controller = DocBrowserContentController(workspace),
            data = item as DocBrowserWsItem
        )
    }
}
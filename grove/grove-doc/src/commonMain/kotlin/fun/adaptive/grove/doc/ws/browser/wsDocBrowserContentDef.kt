package `fun`.adaptive.grove.doc.ws.browser

import `fun`.adaptive.grove.doc.app.GroveDocWsModule
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun wsDocBrowserContentDef(module: GroveDocWsModule<*>) {
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
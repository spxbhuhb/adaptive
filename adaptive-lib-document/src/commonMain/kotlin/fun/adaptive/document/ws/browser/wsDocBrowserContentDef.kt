package `fun`.adaptive.document.ws.browser

import `fun`.adaptive.document.ws.DocWsContext
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun wsDocBrowserContentDef(context: DocWsContext) {
    val workspace = context.workspace

    workspace.addContentPaneBuilder(DocWsContext.WSIT_DOC_ITEM) { item ->
        WsPane(
            UUID(),
            workspace,
            item.name,
            context[item].icon,
            WsPanePosition.Center,
            DocWsContext.WSPANE_DOC_BROWSER_CONTENT,
            controller = DocBrowserContentController(workspace),
            data = item as DocBrowserWsItem
        )
    }
}
package `fun`.adaptive.document.ws.browser

import `fun`.adaptive.adaptive_lib_document.generated.resources.book_3
import `fun`.adaptive.adaptive_lib_document.generated.resources.documentation
import `fun`.adaptive.document.ws.DocWsContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.builtin.collapseAll
import `fun`.adaptive.ui.builtin.expandAll
import `fun`.adaptive.ui.builtin.unfold_less
import `fun`.adaptive.ui.builtin.unfold_more
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneAction
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun wsDocBrowserToolDef(context: DocWsContext): WsPane<Unit, DocBrowserToolController> {

    val config = DocBrowserConfig(
        Strings.documentation,
        Graphics.book_3,
        DocWsContext.WSIT_DOC_ITEM
    )

    val controller = DocBrowserToolController(context.workspace, config)

    config.controller = controller

    val pane = WsPane(
        UUID(),
        Strings.documentation,
        Graphics.book_3,
        WsPanePosition.LeftMiddle,
        DocWsContext.WSPANE_DOC_BROWSER_TOOL,
        actions = listOf(
            WsPaneAction(Graphics.unfold_more, Strings.expandAll, Unit) { controller.expandAll() },
            WsPaneAction(Graphics.unfold_less, Strings.collapseAll, Unit) { controller.collapseAll() },
        ),
        data = Unit,
        controller = controller
    )

    context.io {
        controller.start()
    }

    return pane
}
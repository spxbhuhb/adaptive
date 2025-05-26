package `fun`.adaptive.grove.doc.ws.browser

import `fun`.adaptive.grove.doc.app.GroveDocWsModule
import `fun`.adaptive.grove.doc.generated.resources.book_3
import `fun`.adaptive.grove.doc.generated.resources.documentation
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.collapseAll
import `fun`.adaptive.ui.generated.resources.expandAll
import `fun`.adaptive.ui.generated.resources.unfold_less
import `fun`.adaptive.ui.generated.resources.unfold_more
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneAction
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun MultiPaneWorkspace.wsDocBrowserToolDef(module: GroveDocWsModule<*>) {

    val controller = DocBrowserToolViewBackend(this)

    + WsPane(
        UUID(),
        this,
        Strings.documentation,
        Graphics.book_3,
        WsPanePosition.LeftMiddle,
        module.WSPANE_DOC_BROWSER_TOOL,
        actions = listOf(
            WsPaneAction(Graphics.unfold_more, Strings.expandAll, Unit) { controller.expandAll() },
            WsPaneAction(Graphics.unfold_less, Strings.collapseAll, Unit) { controller.collapseAll() },
        ),
        viewBackend = controller
    )
}
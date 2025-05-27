package `fun`.adaptive.grove.doc.ws.browser

import `fun`.adaptive.grove.doc.app.GroveDocWsModule
import `fun`.adaptive.grove.doc.model.avDomain
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.generated.resources.menu_book
import `fun`.adaptive.ui.mpw.model.Pane
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValue.Companion.checkSpec
import `fun`.adaptive.value.util.checkValue

fun wsDocBrowserContentDef(module: GroveDocWsModule<*>) {
    val workspace = module.workspace

    workspace.addContentPaneBuilder(avDomain.node) { item ->
        checkValue(item)

        Pane(
            UUID(),
            workspace,
            item.nameLike,
            Graphics.menu_book,
            PanePosition.Center,
            module.WSPANE_DOC_BROWSER_CONTENT,
            viewBackend = DocBrowserContentViewBackend(workspace, item.checkSpec())
        )
    }
}
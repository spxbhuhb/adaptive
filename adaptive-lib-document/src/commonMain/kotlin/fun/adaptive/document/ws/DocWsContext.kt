package `fun`.adaptive.document.ws

import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsContext

class DocWsContext(
    override val workspace: Workspace
) : WsContext {

    companion object {
        const val WSIT_DOC_ITEM = "doc:document"

        const val WSPANE_DOC_BROWSER_TOOL = "doc:browser:tool"
        const val WSPANE_DOC_BROWSER_CONTENT = "doc:browser:content"
    }
}
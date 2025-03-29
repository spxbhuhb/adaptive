package `fun`.adaptive.document

import `fun`.adaptive.adaptive_lib_document.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.document.ws.DocWsContext
import `fun`.adaptive.document.ws.browser.wsDocBrowserContentDef
import `fun`.adaptive.document.ws.browser.wsDocBrowserToolDef
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.builtin.menu_book
import `fun`.adaptive.ui.workspace.Workspace

class DocWsModule<WT : Workspace> : AppModule<WT>() {

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter) {
        + DocFragmentFactory
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {

        val context = DocWsContext(this)

        contexts += context

        toolPanes += wsDocBrowserToolDef(context)
        wsDocBrowserContentDef(context)

        addItemConfig(DocWsContext.WSIT_DOC_ITEM, Graphics.menu_book)

    }
}
package `fun`.adaptive.document

import `fun`.adaptive.adaptive_lib_document.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.document.ws.browser.wsDocBrowserContentDef
import `fun`.adaptive.document.ws.browser.wsDocBrowserTool
import `fun`.adaptive.document.ws.browser.wsDocBrowserToolDef
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.model.NamedItemType
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.builtin.menu_book
import `fun`.adaptive.ui.workspace.Workspace

class DocWsModule<WT : Workspace> : AppModule<WT>() {

    val WSIT_DOC_ITEM: NamedItemType
        get() = "doc:document"

    val WSPANE_DOC_BROWSER_TOOL : FragmentKey
        get() = "doc:browser:tool"

    val WSPANE_DOC_BROWSER_CONTENT : FragmentKey
        get() = "doc:browser:content"

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(WSPANE_DOC_BROWSER_TOOL, ::wsDocBrowserTool)
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {

        addItemConfig(WSIT_DOC_ITEM, Graphics.menu_book)

        wsDocBrowserToolDef(this@DocWsModule)
        wsDocBrowserContentDef(this@DocWsModule)

    }
}
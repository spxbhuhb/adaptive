package `fun`.adaptive.document.app

import `fun`.adaptive.document.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.document.ws.browser.wsDocBrowserContentDef
import `fun`.adaptive.document.ws.browser.wsDocBrowserTool
import `fun`.adaptive.document.ws.browser.wsDocBrowserToolDef
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.model.NamedItemType
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.generated.resources.menu_book
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import kotlin.collections.plusAssign

class DocWsModule<WT : MultiPaneWorkspace> : AppModule<WT>() {

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
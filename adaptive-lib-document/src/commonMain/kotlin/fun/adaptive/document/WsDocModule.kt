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

class WsDocModule(
    val loadStrings: Boolean = true
) : AppModule<Workspace>() {

    override suspend fun loadResources() {
        if (loadStrings) {
            commonMainStringsStringStore0.load()
        }
    }

    override fun AdaptiveAdapter.init() {
        fragmentFactory += DocFragmentFactory
    }

    override fun Workspace.init() {

        val context = DocWsContext(this)

        contexts += context

        toolPanes += wsDocBrowserToolDef(context)
        wsDocBrowserContentDef(context)

        addItemConfig(DocWsContext.WSIT_DOC_ITEM, Graphics.menu_book)

    }
}
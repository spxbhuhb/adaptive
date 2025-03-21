package `fun`.adaptive.document

import `fun`.adaptive.adaptive_lib_document.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.document.ws.DocWsContext
import `fun`.adaptive.document.ws.browser.wsDocBrowserContentDef
import `fun`.adaptive.document.ws.browser.wsDocBrowserToolDef
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.builtin.menu_book
import `fun`.adaptive.ui.workspace.Workspace

suspend fun docCommon(loadStrings: Boolean = true) {
    if (loadStrings) {
        println("stuff")
        commonMainStringsStringStore0.load()
    }
}

fun AbstractAuiAdapter<*, *>.docCommon() {
    fragmentFactory += DocFragmentFactory
}

fun Workspace.docCommon() {
    val context = DocWsContext(this)

    contexts += context

    toolPanes += wsDocBrowserToolDef(context)
    wsDocBrowserContentDef(context)

    addItemConfig(DocWsContext.WSIT_DOC_ITEM, Graphics.menu_book)

}

package `fun`.adaptive.document

import `fun`.adaptive.document.ws.DocWsContext
import `fun`.adaptive.document.ws.browser.wsDocBrowserTool
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory


object DocFragmentFactory : FoundationFragmentFactory() {
    init {
        add(DocWsContext.WSPANE_DOC_BROWSER_TOOL, ::wsDocBrowserTool)
    }
}
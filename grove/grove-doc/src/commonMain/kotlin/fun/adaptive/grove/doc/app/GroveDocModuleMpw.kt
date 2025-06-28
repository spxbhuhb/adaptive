package `fun`.adaptive.grove.doc.app

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.grove.doc.generated.resources.book_3
import `fun`.adaptive.grove.doc.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.grove.doc.generated.resources.documentation
import `fun`.adaptive.grove.doc.model.groveDocDomain
import `fun`.adaptive.grove.doc.ui.*
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.ui.generated.resources.menu_book
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.ui.value.iconCache
import `fun`.adaptive.utility.UUID

class GroveDocModuleMpw<FW : MultiPaneWorkspace, BW : AbstractWorkspace> : GroveDocModule<FW, BW>() {

    val WSPANE_DOC_BROWSER_TOOL: FragmentKey
        get() = "grove:doc:tool"

    val WSPANE_DOC_BROWSER_CONTENT: FragmentKey
        get() = "grove:doc:content"

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(WSPANE_DOC_BROWSER_TOOL, ::docToolPane)
        add(WSPANE_DOC_BROWSER_CONTENT, ::docContentPane)

        iconCache[groveDocDomain.node] = Graphics.menu_book
    }

    override fun frontendWorkspaceInit(workspace: FW, session: Any?) = with(workspace) {

        val contentPaneDef = PaneDef(
            UUID("ff694b37-8296-45e2-ba24-870886774730"),
            "",
            Graphics.menu_book,
            PanePosition.Center,
            WSPANE_DOC_BROWSER_CONTENT,
        )

        addContentPaneBuilder(
            groveDocDomain.node,
            { type, item -> item as? GroveDocContentItem }
        ) { item ->
            DocContentViewBackend(workspace, contentPaneDef, item)
        }

        val toolPaneDef = PaneDef(
            UUID("83687ee3-c871-44f1-9b91-6289e59bbbc0"),
            Strings.documentation,
            Graphics.book_3,
            PanePosition.LeftMiddle,
            WSPANE_DOC_BROWSER_TOOL
        )

        val toolBackend = DocToolViewBackend(workspace, toolPaneDef)

        addToolPane { toolBackend }
        addUrlResolver(toolBackend)
    }

}
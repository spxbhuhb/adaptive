package `fun`.adaptive.grove.doc.app

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.grove.doc.generated.resources.book_3
import `fun`.adaptive.grove.doc.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.grove.doc.generated.resources.documentation
import `fun`.adaptive.grove.doc.model.GroveDocSpec
import `fun`.adaptive.grove.doc.model.avDomain
import `fun`.adaptive.grove.doc.ui.DocContentViewBackend
import `fun`.adaptive.grove.doc.ui.DocToolViewBackend
import `fun`.adaptive.grove.doc.ui.docContentPane
import `fun`.adaptive.grove.doc.ui.docToolPane
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.generated.resources.menu_book
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.ui.value.iconCache
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValue.Companion.checkSpec
import `fun`.adaptive.value.util.asValue
import `fun`.adaptive.value.util.asValueOrNull
import `fun`.adaptive.value.util.checkValue

class GroveDocWsModule<WT : MultiPaneWorkspace> : AppModule<WT>() {

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

        iconCache[avDomain.node] = Graphics.menu_book
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {

        val contentPaneDef = PaneDef(
            UUID("ff694b37-8296-45e2-ba24-870886774730"),
            "",
            Graphics.menu_book,
            PanePosition.Center,
            WSPANE_DOC_BROWSER_CONTENT,
        )

        addContentPaneBuilder(
            avDomain.node,
            { asValueOrNull<GroveDocSpec>(it, avDomain.node) }
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

        addToolPane {
            DocToolViewBackend(workspace, toolPaneDef)
        }

    }
}
package `fun`.adaptive.grove.doc.app

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.grove.doc.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.grove.doc.generated.resources.documentation
import `fun`.adaptive.grove.doc.generated.resources.reference
import `fun`.adaptive.grove.doc.model.groveDocDomain
import `fun`.adaptive.grove.doc.ui.*
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.ui.badge.BadgeTheme
import `fun`.adaptive.ui.badge.BadgeTheme.Companion.badgeThemeMap
import `fun`.adaptive.ui.generated.resources.menu_book
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.ui.value.iconCache
import `fun`.adaptive.utility.UUID

class GroveDocModuleMpw<FW : MultiPaneWorkspace, BW : AbstractWorkspace> : GroveDocModule<FW, BW>() {

    companion object {
        val DOC_TOOL: FragmentKey
            get() = "grove:doc:doc-tool"

        val REF_TOOL: FragmentKey
            get() = "grove:doc:reference-tool"

        val DOC_CONTENT: FragmentKey
            get() = "grove:doc:content"

        val INLINE_DEFINITION: FragmentKey
            get() = "grove:doc:inline-definition"

    }

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(DOC_TOOL, ::docToolPane)
        add(REF_TOOL, ::referenceToolPane)
        add(DOC_CONTENT, ::docContentPane)
        add(INLINE_DEFINITION, ::inlineDefinition)

        badgeThemeMap[groveDocDomain.outdated] = BadgeTheme.error
        badgeThemeMap[groveDocDomain.review] = BadgeTheme.warning
        badgeThemeMap[groveDocDomain.todo] = BadgeTheme.important
        badgeThemeMap[groveDocDomain.planning] = BadgeTheme.important
        badgeThemeMap[groveDocDomain.proposal] = BadgeTheme.important

        iconCache[groveDocDomain.node] = Graphics.menu_book
    }

    override fun frontendWorkspaceInit(workspace: FW, session: Any?) = with(workspace) {

        val contentPaneDef = PaneDef(
            UUID("ff694b37-8296-45e2-ba24-870886774730"),
            "",
            Graphics.menu_book,
            PanePosition.Center,
            DOC_CONTENT,
        )

        addContentPaneBuilder(
            groveDocDomain.node,
            { type, item -> item as? GroveDocContentItem }
        ) { item ->
            DocContentViewBackend(workspace, contentPaneDef, item)
        }

        val docToolPaneDef = PaneDef(
            UUID("add25359-772e-45d8-bdf1-2f53ea9e1634"),
            Strings.documentation + " - v" + application.version,
            Graphics.documentation,
            PanePosition.LeftTop,
            DOC_TOOL
        )

        val docToolBackend = DocToolViewBackend(workspace, docToolPaneDef)

        addToolPane { docToolBackend }

        val referenceToolPaneDef = PaneDef(
            UUID("83687ee3-c871-44f1-9b91-6289e59bbbc0"),
            Strings.reference,
            Graphics.reference,
            PanePosition.LeftMiddle,
            REF_TOOL
        )

        val referenceToolBackend = ReferenceToolViewBackend(workspace, referenceToolPaneDef)

        addToolPane { referenceToolBackend }
    }

}
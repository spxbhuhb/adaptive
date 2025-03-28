package `fun`.adaptive.site

import `fun`.adaptive.app.ws.BasicAppWsModule
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.ui.workspace.Workspace

fun buildWorkspace(adapter: AdaptiveAdapter): Workspace {

    val workspace = Workspace(adapter.backend)

    workspace.siteCommon()

    workspace.updateSplits()

    workspace.addContent(BasicAppWsModule.HOME_CONTENT_ITEM, emptySet())

    return workspace
}
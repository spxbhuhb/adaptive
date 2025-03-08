package `fun`.adaptive.site

import `fun`.adaptive.ui.workspace.model.Workspace

fun buildWorkspace(): Workspace {

    val workspace = Workspace()

    workspace.siteCommon()

    workspace.updateSplits()

    return workspace
}
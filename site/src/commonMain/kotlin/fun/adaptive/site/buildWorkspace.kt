package `fun`.adaptive.site

import `fun`.adaptive.ui.workspace.Workspace

fun buildWorkspace(): Workspace {

    val workspace = Workspace()

    workspace.siteCommon()

    workspace.updateSplits()

    return workspace
}
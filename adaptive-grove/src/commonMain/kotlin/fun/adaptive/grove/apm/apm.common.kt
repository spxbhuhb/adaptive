package `fun`.adaptive.grove.apm

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.grove.resources.folder
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.WorkspacePane
import `fun`.adaptive.ui.workspace.WorkspacePanePosition
import `fun`.adaptive.utility.UUID

const val apmProjectPaneKey = "grove:apm:project"

fun AdaptiveAdapter.groveApmCommon() {
    fragmentFactory += arrayOf(ApmPaneFactory)
}

fun Workspace.groveApmCommon() {

    contexts += ApmContext(this)

    panes += WorkspacePane(
        UUID(),
        "Project",
        Graphics.folder,
        WorkspacePanePosition.LeftTop,
        apmProjectPaneKey
    )

}
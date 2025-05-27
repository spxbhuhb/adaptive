package `fun`.adaptive.grove.apm

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.grove.generated.resources.folder
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.UnitPaneViewBackend
import `fun`.adaptive.ui.mpw.model.Pane
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.utility.UUID

fun AdaptiveAdapter.groveApmCommon() {
    fragmentFactory += arrayOf(ApmPaneFactory)
}

fun MultiPaneWorkspace.groveApmCommon() {

    contexts += ApmWsContext(this)

    toolPanes += Pane(
        UUID(),
        workspace = this,
        "Project",
        Graphics.folder,
        PanePosition.LeftTop,
        ApmWsContext.APM_PROJECT_TOOL_KEY,
        viewBackend = UnitPaneViewBackend(this)
    )

}
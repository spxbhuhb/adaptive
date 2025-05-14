package `fun`.adaptive.grove.apm

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.grove.generated.resources.folder
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneController
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun AdaptiveAdapter.groveApmCommon() {
    fragmentFactory += arrayOf(ApmPaneFactory)
}

fun MultiPaneWorkspace.groveApmCommon() {

    contexts += ApmWsContext(this)

    toolPanes += WsPane(
        UUID(),
        workspace = this,
        "Project",
        Graphics.folder,
        WsPanePosition.LeftTop,
        ApmWsContext.APM_PROJECT_TOOL_KEY,
        data = Unit,
        controller = WsUnitPaneController(this)
    )

}
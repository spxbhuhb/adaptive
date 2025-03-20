package `fun`.adaptive.grove.apm

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.grove.resources.folder
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneController
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun AdaptiveAdapter.groveApmCommon() {
    fragmentFactory += arrayOf(ApmPaneFactory)
}

fun Workspace.groveApmCommon() {

    contexts += ApmWsContext(this)

    toolPanes += WsPane(
        UUID(),
        "Project",
        Graphics.folder,
        WsPanePosition.LeftTop,
        ApmWsContext.APM_PROJECT_TOOL_KEY,
        data = Unit,
        controller = WsUnitPaneController()
    )

}
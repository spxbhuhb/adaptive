package `fun`.adaptive.app.ws.admin

import `fun`.adaptive.lib_app.generated.resources.administration
import `fun`.adaptive.lib_app.generated.resources.local_police
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneViewBackend
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun MultiPaneWorkspace.wsAppAdminToolDef(module: AppAdminWsModule<*>) {

    + WsPane(
        UUID(),
        this,
        Strings.administration,
        Graphics.local_police,
        WsPanePosition.RightMiddle,
        module.ADMIN_TOOL_KEY,
        WsUnitPaneViewBackend(this),
        displayOrder = Int.MAX_VALUE
    )

}
package `fun`.adaptive.app.ws.admin

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.administration
import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.local_police
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneController
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun Workspace.wsAppAdminToolDef(module: AppAdminWsModule<*>) {

    + WsPane(
        UUID(),
        this,
        Strings.administration,
        Graphics.local_police,
        WsPanePosition.RightMiddle,
        module.ADMIN_TOOL_KEY,
        Unit,
        WsUnitPaneController(this),
        displayOrder = Int.MAX_VALUE
    )

}
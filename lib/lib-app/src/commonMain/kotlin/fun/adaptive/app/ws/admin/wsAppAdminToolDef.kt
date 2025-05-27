package `fun`.adaptive.app.ws.admin

import `fun`.adaptive.lib_app.generated.resources.administration
import `fun`.adaptive.lib_app.generated.resources.local_police
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.UnitPaneViewBackend
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.utility.UUID

fun MultiPaneWorkspace.wsAppAdminToolDef(module: AppAdminWsModule<*>) {

    + PaneDef(
        UUID(),
        this,
        Strings.administration,
        Graphics.local_police,
        PanePosition.RightMiddle,
        module.ADMIN_TOOL_KEY,
        UnitPaneViewBackend(this),
        displayOrder = Int.MAX_VALUE
    )

}
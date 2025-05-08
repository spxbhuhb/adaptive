package `fun`.adaptive.iot.node.ui.admin

import `fun`.adaptive.app.ws.addAdminItem
import `fun`.adaptive.iot.node.app.SpxbDriverWsModule
import `fun`.adaptive.iot_lib_spxb.generated.resources.systemSettings
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.settings
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun Workspace.wsAppSystemSettingsDef(
    module: SpxbDriverWsModule<*>
) {

    addAdminItem(module.SYSTEM_SETTINGS_ITEM)

    addContentPaneBuilder(module.SYSTEM_SETTINGS_KEY) {
        WsPane(
            UUID(),
            workspace = this,
            Strings.systemSettings,
            Graphics.settings,
            WsPanePosition.Center,
            module.SYSTEM_SETTINGS_KEY,
            module.SYSTEM_SETTINGS_ITEM,
            AppSystemSettingsController(module)
        )
    }

}

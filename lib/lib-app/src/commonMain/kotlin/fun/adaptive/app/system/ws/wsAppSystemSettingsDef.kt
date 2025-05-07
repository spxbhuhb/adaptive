package `fun`.adaptive.app.system.ws

import `fun`.adaptive.app.system.app.AppSystemWsModule
import `fun`.adaptive.app.ws.addAdminItem
import `fun`.adaptive.lib_app.generated.resources.appSystemSettings
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.settings
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun Workspace.wsAppSystemSettingsDef(
    module: AppSystemWsModule<*>
) {

    addAdminItem(module.SYSTEM_SETTINGS_ITEM)

    addContentPaneBuilder(module.SYSTEM_SETTINGS_KEY) {
        WsPane(
            UUID(),
            workspace = this,
            Strings.appSystemSettings,
            Graphics.settings,
            WsPanePosition.Center,
            module.SYSTEM_SETTINGS_KEY,
            module.SYSTEM_SETTINGS_ITEM,
            SystemSettingsController(module)
        )
    }

}

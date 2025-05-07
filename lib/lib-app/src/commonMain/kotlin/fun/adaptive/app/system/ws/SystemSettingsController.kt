package `fun`.adaptive.app.system.ws

import `fun`.adaptive.app.system.app.AppSystemWsModule
import `fun`.adaptive.app.ws.admin.AppAdminWsModule
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController

class SystemSettingsController(
    module: AppSystemWsModule<*>
) : WsSingularPaneController(module.workspace, module.SYSTEM_SETTINGS_ITEM) {

}
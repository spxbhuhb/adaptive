package `fun`.adaptive.app.system.app

import `fun`.adaptive.app.system.ws.wsAppSystemSettings
import `fun`.adaptive.app.system.ws.wsAppSystemSettingsDef
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.lib_app.generated.resources.appSystemSettings
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.settings
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import kotlin.getValue

class AppSystemWsModule<WT : Workspace> : AppSystemModule<WT>() {

    val SYSTEM_SETTINGS_KEY : FragmentKey = "app:ws:admin:system:settings"
    val SYSTEM_SETTINGS_ITEM by lazy { SingularWsItem(Strings.appSystemSettings, SYSTEM_SETTINGS_KEY, Graphics.settings) }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(SYSTEM_SETTINGS_KEY, ::wsAppSystemSettings)
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        wsAppSystemSettingsDef(this@AppSystemWsModule)
    }
}
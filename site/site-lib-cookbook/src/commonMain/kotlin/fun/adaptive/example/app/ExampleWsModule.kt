package `fun`.adaptive.example.app

import `fun`.adaptive.example.ws.wsExampleSettings
import `fun`.adaptive.example.ws.wsExampleSettingsDef
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.settings
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.SingularWsItem

class ExampleWsModule<WT : Workspace> : ExampleModule<WT>() {

    val EXAMPLE_SETTINGS_KEY : FragmentKey = "app:ws:example:settings"
    val EXAMPLE_SETTINGS_ITEM by lazy { SingularWsItem(Strings.settings, EXAMPLE_SETTINGS_KEY, Graphics.settings) }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(EXAMPLE_SETTINGS_KEY, ::wsExampleSettings)
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        wsExampleSettingsDef(this@ExampleWsModule)
    }

}
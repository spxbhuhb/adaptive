package my.project.example.app

import my.project.example.ws.wsExampleSettings
import my.project.example.ws.wsExampleSettingsDef
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.settings
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.SingularPaneItem

class ExampleWsModule<WT : MultiPaneWorkspace> : ExampleModule<WT>() {

    val EXAMPLE_SETTINGS_KEY : FragmentKey = "app:ws:example:settings"
    val EXAMPLE_SETTINGS_ITEM by lazy { SingularPaneItem(Strings.settings, EXAMPLE_SETTINGS_KEY, Graphics.settings) }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(EXAMPLE_SETTINGS_KEY, ::wsExampleSettings)
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        wsExampleSettingsDef(this@ExampleWsModule)
    }

}
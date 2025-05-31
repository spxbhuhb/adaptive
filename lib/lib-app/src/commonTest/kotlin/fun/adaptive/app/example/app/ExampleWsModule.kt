package `fun`.adaptive.app.example.app

import `fun`.adaptive.app.example.ws.wsExampleSettings
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.ui.generated.resources.settings
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.SingularPaneItem

class ExampleWsModule<FW : MultiPaneWorkspace, BW : AbstractWorkspace> : ExampleModule<FW, BW>() {

    // Define frontend fragment keys and singular workspace items here
    val EXAMPLE_SETTINGS_KEY : FragmentKey = "app:ws:admin:system:settings"
    val EXAMPLE_SETTINGS_ITEM by lazy { SingularPaneItem(Strings.settings, EXAMPLE_SETTINGS_KEY, Graphics.settings) }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        // Register UI fragments
        add(EXAMPLE_SETTINGS_KEY, ::wsExampleSettings)
    }

    override fun frontendWorkspaceInit(workspace: FW, session: Any?) = with(workspace) {
        // Register workspace pane/item definitions
        // wsExampleSettingsDef(this@ExampleWsModule)
    }

}
package `fun`.adaptive.app.example.ui.mpw

import `fun`.adaptive.app.example.app.ExampleModule
import `fun`.adaptive.app.example.ws.wsExampleSettings
import `fun`.adaptive.app.ui.mpw.addAdminPlugin
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.ui.generated.resources.example
import `fun`.adaptive.ui.generated.resources.settings
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.UnitSingularContentViewBackend
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.ui.mpw.model.SingularPaneItem
import `fun`.adaptive.utility.UUID

class ExampleAdminToolModuleMpw<FW : MultiPaneWorkspace, BW : AbstractWorkspace> : ExampleModule<FW, BW>() {

    val EXAMPLE_PLUGIN_KEY : FragmentKey = "example:admin-tool:plugin"
    val EXAMPLE_PLUGIN_ITEM by lazy { SingularPaneItem(Strings.example, EXAMPLE_PLUGIN_KEY, Graphics.example) }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(EXAMPLE_PLUGIN_KEY, ::adminToolPluginExample)
    }

    override fun frontendWorkspaceInit(workspace: FW, session: Any?) = with(workspace) {

        addAdminPlugin(EXAMPLE_PLUGIN_ITEM)

        addSingularContentPane(EXAMPLE_PLUGIN_ITEM) {
            UnitSingularContentViewBackend(
                workspace,
                PaneDef(
                    UUID(),
                    Strings.settings,
                    Graphics.settings,
                    PanePosition.Center,
                    EXAMPLE_PLUGIN_KEY
                ),
                EXAMPLE_PLUGIN_ITEM
            )
        }
    }

}
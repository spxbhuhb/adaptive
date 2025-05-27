package `fun`.adaptive.app.ws.inspect

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace

class AppInspectWsModule<WT : MultiPaneWorkspace> : AppModule<WT>() {

    val INSPECT_TOOL_KEY: FragmentKey
        get() = "app:ws:inspect:tool"

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
            add(INSPECT_TOOL_KEY, ::wsAppInspectTool)
        }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        wsAppInspectToolDef(this@AppInspectWsModule)
    }

}
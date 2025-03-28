package `fun`.adaptive.app.ws.inspect

import `fun`.adaptive.app.ws.BasicAppWsModule
import `fun`.adaptive.app.ws.inspect.BasicAppInspectWsModule.Companion.INSPECT_TOOL_KEY
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object WsAppInspectFragmentFactory : FoundationFragmentFactory() {
    init {
        with(BasicAppWsModule) {
            add(INSPECT_TOOL_KEY, ::wsAppInspectTool)
        }
    }
}
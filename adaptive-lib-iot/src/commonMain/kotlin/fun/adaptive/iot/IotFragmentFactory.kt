package `fun`.adaptive.iot

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.iot.ws.wsSpaceContentPane
import `fun`.adaptive.iot.ws.wsSpaceToolPane

object IotFragmentFactory : FoundationFragmentFactory() {
    init {
        add(AioWsContext.WSPANE_SPACE_TOOL, ::wsSpaceToolPane)
        add(AioWsContext.WSPANE_SPACE_CONTENT, ::wsSpaceContentPane)
    }
}
package `fun`.adaptive.iot.ws

import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsContext
import `fun`.adaptive.ui.value.AvNameCache

class AioWsContext(
    override val workspace: Workspace
) : WsContext {

    val spaceNameCache = AvNameCache(
        workspace.backend,
        workspace.transport,
        workspace.scope,
        SpaceMarkers.SPACE
    ).also {
        it.start()
    }

    companion object {
        const val WSIT_DEVICE = "aio:device"
        const val WSIT_SPACE = "aio:space"
        const val WSIT_POINT = "aio:point"
        const val WSIT_HISTORY = "aio:history"

        const val WSPANE_SPACE_TOOL = "aio:space:tool"
        const val WSPANE_SPACE_CONTENT = "aio:space:content"

        const val WSPANE_DEVICE_TOOL = "aio:device:tool"
        const val WSPANE_DEVICE_CONTENT = "aio:device:content"

        const val WSPANE_RHT_BROWSER_TOOL = "aio:rht:browser:tool"
        const val WSPANE_RHT_BROWSER_CONTENT = "aio:rht:browser:content"

        const val WSPANE_HISTORY_CONTENT = "aio:history:content"
    }

}
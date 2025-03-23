package `fun`.adaptive.iot.ws

import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsContext
import `fun`.adaptive.value.ui.AvNameCache

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

        const val WSIT_MEASUREMENT_LOCATION = "aio:measurement:point"
        const val WSIT_MEASUREMENT_POINT = "aio:measurement:point"
        const val WSIT_PROJECT = "aio:project"
        const val WSIT_SPACE = "aio:space"

        const val WSIT_POINT = "aio:point"

        const val WSPANE_SPACE_TOOL = "aio:space:tool"
        const val WSPANE_SPACE_CONTENT = "aio:space:content"

        const val WSPANE_DEVICE_TOOL = "aio:device:tool"
        const val WSPANE_DEVICE_CONTENT = "aio:device:content"

        const val WSPANE_INFRASTRUCTURE_TOOL = "aio:infrastructure:tool"
        const val WSPANE_INFRASTRUCTURE_CONTENT = "aio:infrastructure:content"

        const val WSPANE_RHT_BROWSER_TOOL = "aio:rht:browser:tool"
        const val WSPANE_RHT_BROWSER_CONTENT = "aio:rht:browser:content"

        const val WSPANE_MEASUREMENT_LOCATION_CONTENT = "aio:measurement:location:content"
    }

}
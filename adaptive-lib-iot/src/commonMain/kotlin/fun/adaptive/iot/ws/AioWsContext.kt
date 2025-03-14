package `fun`.adaptive.iot.ws

import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsContext

class AioWsContext(override val workspace: Workspace) : WsContext {

    companion object {
        const val WSIT_DEVICE = "aio:device"
        const val WSIT_DEVICE_POINT = "aio:device:point"
        const val WSIT_MEASUREMENT_LOCATION = "aio:measurement:point"
        const val WSIT_MEASUREMENT_POINT = "aio:measurement:point"
        const val WSIT_NETWORK = "aio:network"
        const val WSIT_PROJECT = "aio:project"
        const val WSIT_SPACE = "aio:space"
    }

}
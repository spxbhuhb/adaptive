package `fun`.adaptive.iot.lib.zigbee.app

import `fun`.adaptive.iot.app.IotModule.Companion.iotModule
import `fun`.adaptive.iot.device.network.AioDriverDef
import `fun`.adaptive.runtime.AbstractWorkspace

open class ZigbeeDriverWsModule<WT : AbstractWorkspace> : ZigbeeDriverModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) {

        application.iotModule += AioDriverDef(
            driveKey,
            "aio_driver_zigbee",
            driveKey,
            "",
            ""
        )

    }
}
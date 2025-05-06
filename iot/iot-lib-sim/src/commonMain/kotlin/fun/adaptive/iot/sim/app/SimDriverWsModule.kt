package `fun`.adaptive.iot.sim.app

import `fun`.adaptive.iot.app.IotModule.Companion.iotModule
import `fun`.adaptive.iot.device.network.AioDriverDef
import `fun`.adaptive.runtime.AbstractWorkspace

class SimDriverWsModule<WT : AbstractWorkspace> : SimClientModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) {
        application.iotModule += AioDriverDef(
            driverKey,
            "aio_driver_sim",
            driverKey,
            networkConfigKey,
            networkConfigKey
        )
    }

}
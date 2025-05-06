package `fun`.adaptive.iot.node.app

import `fun`.adaptive.iot.app.IotModule.Companion.iotModule
import `fun`.adaptive.iot.device.network.AioDriverDef
import `fun`.adaptive.iot_lib_spxb.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.runtime.AbstractWorkspace

class SpxbDriverWsModule<WT : AbstractWorkspace> : SpxbClientModule<WT>() {

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

    override fun workspaceInit(workspace: WT, session: Any?) {
        application.iotModule += AioDriverDef(
            driverKey,
            "aio_driver_spxb",
            driverKey,
            networkConfigKey,
            networkConfigKey
        )
    }

}
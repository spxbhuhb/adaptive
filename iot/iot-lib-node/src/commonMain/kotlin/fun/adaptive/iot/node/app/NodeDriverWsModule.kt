package `fun`.adaptive.iot.node.app

import `fun`.adaptive.iot.app.IotModule.Companion.iotModule
import `fun`.adaptive.iot.device.network.AioDriverDef
import `fun`.adaptive.iot_lib_node.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.runtime.AbstractWorkspace

class NodeDriverWsModule<WT : AbstractWorkspace> : NodeDriverModule<WT>() {

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

    override fun workspaceInit(workspace: WT, session: Any?) {
        application.iotModule += AioDriverDef(
            driverKey,
            "aio_driver_node",
            driverKey,
            "",
            ""
        )
    }

}
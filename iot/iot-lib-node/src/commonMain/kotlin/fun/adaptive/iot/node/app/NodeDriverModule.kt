package `fun`.adaptive.iot.node.app

import `fun`.adaptive.iot.device.network.AioDriverDef
import `fun`.adaptive.iot.node.spec.NodeControllerSpec
import `fun`.adaptive.iot.node.spec.NodeNetworkSpec
import `fun`.adaptive.iot.node.spec.NodePointSpec
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.utility.ComponentKey
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.WireFormatRegistry

open class NodeDriverModule<WT : AbstractWorkspace> : AppModule<WT>() {

    val driverKey: ComponentKey
        get() = "aio:driver:node"

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        + NodeNetworkSpec
        + NodeControllerSpec
        + NodePointSpec
    }

}
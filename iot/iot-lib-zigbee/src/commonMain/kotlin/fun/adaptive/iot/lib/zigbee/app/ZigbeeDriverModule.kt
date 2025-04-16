package `fun`.adaptive.iot.lib.zigbee.app

import `fun`.adaptive.iot.lib.zigbee.model.ZigBeeControllerSpec
import `fun`.adaptive.iot.lib.zigbee.model.ZigBeeEndpoint
import `fun`.adaptive.iot.lib.zigbee.model.ZigBeeNetworkSpec
import `fun`.adaptive.iot.lib.zigbee.model.ZigBeePointSpec
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.wireformat.WireFormatRegistry

open class ZigbeeDriverModule<WT : AbstractWorkspace> : AppModule<WT>() {

    val driveKey: String
        get() = "aio:driver:zigbee"

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        + ZigBeeNetworkSpec
        + ZigBeeControllerSpec
        + ZigBeePointSpec
        + ZigBeeEndpoint
    }

}
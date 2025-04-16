package `fun`.adaptive.iot.lib.zigbee

import `fun`.adaptive.iot.lib.zigbee.model.ZigBeeControllerSpec
import `fun`.adaptive.iot.lib.zigbee.model.ZigBeeEndpoint
import `fun`.adaptive.iot.lib.zigbee.model.ZigBeeNetworkSpec
import `fun`.adaptive.iot.lib.zigbee.model.ZigBeePointSpec
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.wireformat.WireFormatRegistry

open class ZigbeeModule<WT : AbstractWorkspace> : AppModule<WT>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        + ZigBeeNetworkSpec
        + ZigBeeControllerSpec
        + ZigBeePointSpec
        + ZigBeeEndpoint
    }

}
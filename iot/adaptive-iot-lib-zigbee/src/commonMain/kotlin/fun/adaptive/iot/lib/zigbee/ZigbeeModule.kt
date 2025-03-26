package `fun`.adaptive.iot.lib.zigbee

import `fun`.adaptive.iot.history.model.*
import `fun`.adaptive.iot.lib.zigbee.model.ZigBeeControllerSpec
import `fun`.adaptive.iot.lib.zigbee.model.ZigBeeNetworkSpec
import `fun`.adaptive.iot.lib.zigbee.model.ZigBeePointSpec
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.value.operation.*
import `fun`.adaptive.wireformat.WireFormatRegistry

open class ZigbeeModule<WT> : AppModule<WT>() {

    override fun WireFormatRegistry.init() {
        this += ZigBeeNetworkSpec
        this += ZigBeeControllerSpec
        this += ZigBeePointSpec
    }

}
package `fun`.adaptive.iot.lib.zigbee.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.Secret
import `fun`.adaptive.iot.device.AioDeviceSpec

@Adat
class ZigBeeNetworkSpec(
    override val notes: String = "",
    override val manufacturer: String? = null,
    override val model: String? = null,
    override val serialNumber: String? = null,
    override val firmwareVersion: String? = null,
    override val hardwareVersion: String? = null,
    override val displayAddress: String? = null,
    val dongleType: String = "EMBER",
    val channel: Int = 0,
    val pan: Int = 0,
    val epan: Long = 0,
    val nwkKey: Secret = "",
    val localEndpointId: Int = 1,
    val maxEndpoints: Int = 0,
    val radioTxPower: Int = 20
) : AioDeviceSpec()
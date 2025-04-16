package `fun`.adaptive.iot.lib.zigbee.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.device.AioDeviceSpec

@Adat
class ZigBeeControllerSpec(
    override val enabled: Boolean = true,
    override val notes: String = "",
    override val manufacturer: String? = null,
    override val model: String? = null,
    override val serialNumber: String? = null,
    override val firmwareVersion: String? = null,
    override val hardwareVersion: String? = null,
    override val displayAddress: String? = null,
    val networkAddress: Int? = null,
    val ieeeAddress: String? = null,
    val endpoints: List<ZigBeeEndpoint> = emptyList()
) : AioDeviceSpec()
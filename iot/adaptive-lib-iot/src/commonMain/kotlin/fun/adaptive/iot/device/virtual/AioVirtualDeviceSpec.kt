package `fun`.adaptive.iot.device.virtual

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.device.AioDeviceSpec

@Adat
class AioVirtualDeviceSpec(
    override val notes: String = "",
    override val manufacturer: String? = null,
    override val model: String? = null,
    override val serialNumber: String? = null,
    override val firmwareVersion: String? = null,
    override val hardwareVersion: String? = null,
    override val displayAddress: String? = null
) : AioDeviceSpec() {

    override val virtual: Boolean
        get() = true

}
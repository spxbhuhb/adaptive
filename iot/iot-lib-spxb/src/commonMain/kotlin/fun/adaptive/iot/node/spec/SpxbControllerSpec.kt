package `fun`.adaptive.iot.node.spec

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.Secret
import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.iot.device.AioDeviceSpec

@Adat
class SpxbControllerSpec(
    override val enabled: Boolean = true,
    override val notes: String = "",
    override val manufacturer: String? = null,
    override val model: String? = null,
    override val serialNumber: String? = null,
    override val firmwareVersion: String? = null,
    override val hardwareVersion: String? = null,
    override val displayAddress: String? = null,
    val upstreamUrl: String? = null,
    val upstreamAccount : String? = null,
    val upstreamPassword : Secret? = null
) : AioDeviceSpec(){

    override fun descriptor() {
        properties {
            upstreamPassword secret true
        }
    }

}
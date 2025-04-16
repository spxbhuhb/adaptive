package `fun`.adaptive.iot.device.network

import `fun`.adaptive.adat.Adat

@Adat
class AioDriverDef(
    val driverKey: String,
    val newNetworkFragmentKey: String,
    val editNetworkFragmentKey: String
)
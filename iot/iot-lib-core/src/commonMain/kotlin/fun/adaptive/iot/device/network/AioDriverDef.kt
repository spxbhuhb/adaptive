package `fun`.adaptive.iot.device.network

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.GraphicsResourceKey
import `fun`.adaptive.resource.StringResourceKey
import `fun`.adaptive.utility.ComponentKey

@Adat
class AioDriverDef(
    val driverKey : ComponentKey,
    val driverNameKey: StringResourceKey,
    val driverIconKey: GraphicsResourceKey,
    val newNetworkKey: FragmentKey,
    val editNetworkKey: FragmentKey
)
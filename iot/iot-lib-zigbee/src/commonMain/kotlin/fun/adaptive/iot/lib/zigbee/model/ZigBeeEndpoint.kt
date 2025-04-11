package `fun`.adaptive.iot.lib.zigbee.model

import `fun`.adaptive.adat.Adat

@Adat
class ZigBeeEndpoint(
    val endpoint: Int,
    val clusters: List<Int> = emptyList()
)
package `fun`.adaptive.iot.lib.zigbee.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.point.AioPointSpec

@Adat
class ZigBeePointSpec(
    override val notes: String = "",
    val endpoint: Int,
    val cluster: Int,
    val attribute: Int,
    val typeId: Int,
    val reportable: Boolean
) : AioPointSpec()
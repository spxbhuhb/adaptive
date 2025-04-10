package `fun`.adaptive.iot.lib.zigbee.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.iot.point.conversion.CurValConversion

@Adat
class ZigBeePointSpec(
    override val displayAddress: String = "",
    override val notes: String = "",
    override val conversion: CurValConversion? = null,
    val endpoint: Int,
    val cluster: Int,
    val attribute: Int,
    val typeId: Int,
    val reportable: Boolean
) : AioPointSpec()
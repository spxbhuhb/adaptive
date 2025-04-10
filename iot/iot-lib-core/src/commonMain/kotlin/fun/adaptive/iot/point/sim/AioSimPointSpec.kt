package `fun`.adaptive.iot.point.sim

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.iot.point.conversion.CurValConversion

@Adat
class AioSimPointSpec(
    override val displayAddress: String = "",
    override val notes: String = "",
    override val conversion: CurValConversion? = null
) : AioPointSpec()
package `fun`.adaptive.iot.test

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.iot.point.conversion.CurValConversion

@Adat
class TestPointSpec(
    override val displayAddress: String = "",
    override val notes: String = "",
    override val conversion: CurValConversion? = null
) : AioPointSpec()
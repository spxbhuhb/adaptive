package `fun`.adaptive.cookbook.test.adat.wireformat

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.wireformat.builtin.EnumWireFormat
import `fun`.adaptive.wireformat.toJson
import kotlin.test.Test
import kotlin.test.assertEquals

class EnumTest {

    @Test
    fun basic() {
        println(TA.adatMetadata.toJson(AdatClassMetadata).decodeToString())

        WireFormatRegistry["fun.adaptive.adat.wireformat.TE"] = EnumWireFormat<TE>(TE.entries)

        val t = TA(TE.V1, setOf(TE.V1, TE.V2))

        val json = t.toJson(TA)
        val readback = TA.fromJson(json)

        assertEquals(t, readback)
    }

}

@Suppress("unused")
@Adat
private class TA(
    val e: TE,
    val eSet: Set<TE>
)

private enum class TE {
    V1,
    V2
}
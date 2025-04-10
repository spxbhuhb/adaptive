package `fun`.adaptive.adat.nocode

import `fun`.adaptive.adat.encodeToJsonString
import kotlin.test.Test
import kotlin.test.assertEquals

class NoCodeAdatClassTest {

    @Test
    fun basic() {
        val t1 = TestClass(12, "a")
        val ncc = NoCodeAdatCompanion(t1.adatCompanion.adatMetadata)
        val nc = ncc.newInstance(arrayOf(12, "a"))

        assertEquals(ncc, nc.adatCompanion)
        assertEquals(t1.adatCompanion.adatMetadata, nc.adatCompanion.adatMetadata)

        val t1Json = t1.encodeToJsonString()
        val ncJson = nc.encodeToJsonString()

        assertEquals(t1Json, ncJson)
    }

}
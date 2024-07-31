package hu.simplexion.adaptive.auto.backend

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.connector.DirectConnector
import hu.simplexion.adaptive.utility.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

@Adat
class TestData(
    val i: Int,
    val s: String
) : AdatClass<TestData>

class AutoInstanceTest {

    @Test
    fun basic() {
        val gid = UUID<AutoBackend>()
        val itemId = LamportTimestamp(0, 0)
        val testData = TestData(12, "ab")

        val i1 = AutoInstance(gid, LamportTimestamp(1, 0), testData)
        val i2 = AutoInstance(gid, LamportTimestamp(2, 0), testData)

        i1 += DirectConnector(i2)
        i2 += DirectConnector(i1)

        i1.modify(itemId, "i", 23)
        assertEquals(23, i2.value.i)

        i2.modify(itemId, "i", 34)
        assertEquals(34, i1.value.i)

        i1.modify(itemId, "s", "cd")
        assertEquals("cd", i2.value.s)

        i2.modify(itemId, "s", "ef")
        assertEquals("ef", i1.value.s)
    }

}
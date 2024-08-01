package hu.simplexion.adaptive.auto.backend

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.AdatCompanion
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.connector.DirectConnector
import hu.simplexion.adaptive.utility.UUID
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@Adat
class TestData(
    val i: Int,
    val s: String
) : AdatClass<TestData> {
    companion object : AdatCompanion<TestData>
}

class AutoInstanceTest {

    @Test
    fun basic() {
        val gid = UUID<AutoBackend>()
        val itemId = LamportTimestamp(1, 1)
        val testData = TestData(12, "ab")

        runTest {

            val i1 = AutoInstance(gid, this, itemId, TestData, testData)
            val i2 = AutoInstance(gid, this, LamportTimestamp(2, 0), TestData)

            i1.addPeer(DirectConnector(i2), i2.time)
            i2.addPeer(DirectConnector(i1), i1.time)

            while (i2.value == null) {
                delay(10)
            }

            i1.modify(itemId, "i", 23)
            assertEquals(23, i2.value !!.i)

            i2.modify(itemId, "i", 34)
            assertEquals(34, i1.value !!.i)

            i1.modify(itemId, "s", "cd")
            assertEquals("cd", i2.value !!.s)

            i2.modify(itemId, "s", "ef")
            assertEquals("ef", i1.value !!.s)

        }
    }

}
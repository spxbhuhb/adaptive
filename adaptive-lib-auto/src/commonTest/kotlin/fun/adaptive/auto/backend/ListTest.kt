package `fun`.adaptive.auto.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.LamportTimestamp
import `fun`.adaptive.auto.connector.DirectConnector
import `fun`.adaptive.auto.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class ListTest {

    @Test
    fun basic() {
        val gid = UUID<BackendBase>()
        val testData = TestData(12, "ab")

        runTest {
            val scope = CoroutineScope(Dispatchers.Default)

            val c1 = BackendContext(AutoHandle(gid, 1), scope, ProtoWireFormatProvider(), TestData.adatMetadata, TestData.adatWireFormat, true, LamportTimestamp(1, 1))
            val b1 = ListBackend(c1)
            val f1 = AdatClassListFrontend<TestData>(b1, TestData)
            b1.frontEnd = f1

            val c2 = BackendContext(AutoHandle(gid, 2), scope, ProtoWireFormatProvider(), TestData.adatMetadata, TestData.adatWireFormat, true, LamportTimestamp(2, 0))
            val b2 = ListBackend(c2)
            val f2 = AdatClassListFrontend<TestData>(b2, TestData)
            b2.frontEnd = f2

            b1.addPeer(DirectConnector(b2), c2.time)
            b2.addPeer(DirectConnector(b1), c1.time)

            f1.add(testData)
            f2.assertEquals(f1)

            val itemId1 = f1.values.first().itemId
            f1.modify(itemId1, "i", 23)
            f2.assertEquals(f1)

            f2.modify(itemId1, "i", 34)
            f1.assertEquals(f2)

            val t2 = TestData(54, "ef")
            f1.add(t2)
            f2.assertEquals(f1)

            val t3 = TestData(67, "gh")
            f2.add(t3)
            f1.assertEquals(f2)

            f1.remove(itemId1)
            f2.assertEquals(f1)
        }
    }

    fun <A : AdatClass<A>> AdatClassListFrontend<A>.assertEquals(expected : AdatClassListFrontend<A>) {
        this.backend.assertEquals(expected.backend)
        assertEquals(expected.values, this.values)
    }

    fun ListBackend.assertEquals(expected : ListBackend) {
        assertEquals(expected.additions, this.additions)
        assertEquals(expected.removals, this.removals)
        assertEquals(expected.items.size, this.items.size)
        for ((key, value) in this.items) {
            value.assertEquals(expected.items[key] !!)
        }
    }

    fun PropertyBackend.assertEquals(expected : PropertyBackend) {
        assertEquals(expected.itemId, this.itemId)
        assertContentEquals(expected.values, this.values)
    }
}
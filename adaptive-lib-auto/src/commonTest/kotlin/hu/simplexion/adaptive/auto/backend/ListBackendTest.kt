package hu.simplexion.adaptive.auto.backend

import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.connector.DirectConnector
import hu.simplexion.adaptive.auto.frontend.AdatClassListFrontend
import hu.simplexion.adaptive.auto.model.AutoHandle
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ListBackendTest {

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
            assertEquals(testData, f2.values.first().instance)

            f1.values.first().also {
                f1.modify(it.itemId, "i", 23)
                assertEquals(23, f2.values.first().instance.i)
            }

            f2.values.first().also {
                f2.modify(it.itemId, "i", 34)
                assertEquals(34, f1.values.first().instance.i)
            }

            val t2 = TestData(54, "ef")
            f1.add(t2)
            assertEquals(t2, f2.values[1].instance)

            val t3 = TestData(67, "gh")
            f2.add(t3)
            assertEquals(t3, f1.values[2].instance)

            f1.remove(f1.values.first().itemId)
            assertEquals(2, f2.values.size)
        }
    }

}
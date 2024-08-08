package hu.simplexion.adaptive.auto.backend

import hu.simplexion.adaptive.adat.toArray
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.connector.DirectConnector
import hu.simplexion.adaptive.auto.frontend.AdatClassFrontend
import hu.simplexion.adaptive.auto.model.AutoHandle
import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PropertyBackendTest {

    @Test
    fun basic() {
        val gid = UUID<BackendBase>()
        val itemId = LamportTimestamp(1, 1)
        val testData = TestData(12, "ab")

        runTest {
            val scope = CoroutineScope(Dispatchers.Default)

            val c1 = BackendContext(AutoHandle(gid, 1), scope, ProtoWireFormatProvider(), TestData.adatMetadata, TestData.adatWireFormat, true, LamportTimestamp(1, 1))
            val b1 = PropertyBackend(c1, itemId, null, testData.toArray())
            val f1 = AdatClassFrontend(b1, TestData, testData)
            b1.context.frontEnd = f1

            val c2 = BackendContext(AutoHandle(gid, 2), scope, ProtoWireFormatProvider(), TestData.adatMetadata, TestData.adatWireFormat, true, LamportTimestamp(2, 0))
            val b2 = PropertyBackend(c2, itemId, null, null)
            val f2 = AdatClassFrontend(b2, TestData, null)
            b2.context.frontEnd = f2

            b1.addPeer(DirectConnector(b2), c2.time)
            b2.addPeer(DirectConnector(b1), c1.time)

            while (f2.value == null) {
                delay(10)
            }

            assertEquals(12, f2.value !!.i)

            f1.modify("i", 23)
            assertEquals(23, f2.value !!.i)

            f2.modify("i", 34)
            assertEquals(34, f1.value !!.i)

            f1.modify("s", "cd")
            assertEquals("cd", f2.value !!.s)

            f2.modify("s", "ef")
            assertEquals("ef", f1.value !!.s)

        }
    }

}
package `fun`.adaptive.auto.backend

import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.internal.backend.BackendContext
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.connector.DirectConnector
import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PropertyTest {

    @Test
    fun basic() {
        val gid = UUID<BackendBase>()
        val itemId = LamportTimestamp(1, 1)
        val testData = TestData(12, "ab")

        runTest {
            val scope = CoroutineScope(Dispatchers.Default)
            val logger1 = getLogger("logger.1")
            val logger2 = getLogger("logger.2")

            val c1 = BackendContext(AutoHandle(gid, 1), scope, logger1, ProtoWireFormatProvider(), TestData.adatMetadata, TestData.adatWireFormat, LamportTimestamp(1, 1))
            val b1 = PropertyBackend(c1, itemId, null, testData.toArray())
            val f1 = AdatClassFrontend(b1, TestData, testData, null)
            b1.frontEnd = f1

            val c2 = BackendContext(AutoHandle(gid, 2), scope, logger2, ProtoWireFormatProvider(), TestData.adatMetadata, TestData.adatWireFormat, LamportTimestamp(2, 0))
            val b2 = PropertyBackend(c2, itemId, null, null)
            val f2 = AdatClassFrontend(b2, TestData, null, null)
            b2.frontEnd = f2

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
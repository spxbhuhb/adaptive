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
            b1.context.frontEnd = f1

            val c2 = BackendContext(AutoHandle(gid, 2), scope, ProtoWireFormatProvider(), TestData.adatMetadata, TestData.adatWireFormat, true, LamportTimestamp(2, 0))
            val b2 = ListBackend(c2)
            val f2 = AdatClassListFrontend<TestData>(b2, TestData)
            b2.context.frontEnd = f2

            b1.addPeer(DirectConnector(b2), c2.time)
            b2.addPeer(DirectConnector(b1), c1.time)

            f1.add(testData)
        }
    }

}
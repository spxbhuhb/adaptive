package `fun`.adaptive.auto.test.connection

import `fun`.adaptive.auto.api.autoItemNode
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.auto.test.support.AutoTest.Companion.autoTest
import `fun`.adaptive.auto.test.support.AutoTest.Companion.sync
import `fun`.adaptive.auto.test.support.TestData
import `fun`.adaptive.utility.waitForReal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class ReconnectTest {

    @Test
    fun basic() = autoTest {

        // so we don't have to wait for 20 seconds
        clientTransport.responseTimeout = 1.seconds

        val td = TestData(12, "a")
        val origin = autoItemOrigin(td, serverWorker)
        val node = autoItemNode(clientWorker) { origin.connectInfo(AutoConnectionType.Service) }

        sync(node, origin)

        clientTransport.drop = true

        node.update(td::i to 23)

        waitForReal(2.seconds)
        clientTransport.drop = false

        sync(node, origin)
        assertEquals(23, origin.value.i)
    }

// this test fails, it is a bug:
//    @Test
//    fun doubleChange() = autoTest {
//
//        // so we don't have to wait for 20 seconds
//        clientTransport.responseTimeout = 1.seconds
//        serverTransport.responseTimeout = 1.seconds
//
//        val td = TestData(12, "a")
//        val origin = autoItemOrigin(td, serverWorker)
//        val node = autoItemNode(clientWorker) { origin.connectInfo(AutoConnectionType.Service) }
//
//        sync(node, origin)
//
//        clientTransport.drop = true
//        serverTransport.drop = true
//
//        origin.update(td::s to "b")
//        node.update(td::i to 23)
//
//        waitForReal(2.seconds)
//
//        clientTransport.drop = false
//        serverTransport.drop = false
//
//        sync(node, origin)
//
//        assertEquals(23, node.value.i)
//        assertEquals(23, origin.value.i)
//        assertEquals("b", node.value.s)
//        assertEquals("b", origin.value.s)
//    }
}
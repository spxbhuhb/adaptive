package `fun`.adaptive.auto.test.item.node

import `fun`.adaptive.auto.api.autoItemNode
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.auto.test.support.AutoTest.Companion.autoTest
import `fun`.adaptive.auto.test.support.TestData
import `fun`.adaptive.utility.waitForReal
import kotlinx.coroutines.delay
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class ServiceTest {

    @Test
    fun basic() = autoTest {

        val td = TestData(12, "a")
        val origin = autoItemOrigin(td, serverWorker, trace = true)
        val node = autoItemNode(clientWorker, trace = true) { origin.connectInfo(AutoConnectionType.Service) }

        waitForReal(1.seconds) { node.time.timestamp == origin.time.timestamp }

        origin.update(td::i to 23)
        waitForReal(1.seconds) { node.time.timestamp == origin.time.timestamp }
        assertEquals(23, node.value.i)

        node.update(td::i to 34)
        waitForReal(1.seconds) { node.time.timestamp == origin.time.timestamp }
        assertEquals(34, origin.value.i)
    }

}
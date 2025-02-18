package `fun`.adaptive.auto.test.item.node

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

class ServiceTest {

    @Test
    fun basic() = autoTest {

        val td = TestData(12, "a")
        val origin = autoItemOrigin(td, serverWorker)
        val node = autoItemNode(clientWorker) { origin.connectInfo(AutoConnectionType.Service) }

        sync(node, origin)
        origin.update(td::i to 23)

        sync(node, origin)
        assertEquals(23, node.value.i)

        node.update(td::i to 34)

        sync(node, origin)
        assertEquals(34, origin.value.i)
    }

}
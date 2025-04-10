package `fun`.adaptive.auto.test.collection.node

import `fun`.adaptive.auto.api.autoCollectionNode
import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.auto.api.autoItemNode
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.auto.test.support.AutoTest.Companion.autoTest
import `fun`.adaptive.auto.test.support.TestData
import `fun`.adaptive.auto.test.support.wait
import `fun`.adaptive.utility.waitForReal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class ServiceTest {

    val td12 = TestData(12, "a")
    val td23 = TestData(23, "b")
    val td45 = TestData(45, "c")

    val content_empty = listOf<TestData>()
    val content_12 = listOf(td12)
    val content_23 = listOf(td23)
    val content_12_23 = listOf(td12, td23)
    val content_45_23 = listOf(td45, td23)


    @Test
    fun basic() = autoTest {

        val origin = autoCollectionOrigin(content_12_23, serverWorker)
        val node = autoCollectionNode(clientWorker) { origin.connectInfo() }

        waitForReal(1.seconds) { node.time.timestamp == origin.time.timestamp }

        node.update(td12::i to 45, td12::s to "c") { it == td12 }

        wait(node, origin)

        assertEquals(content_45_23, origin.value)
    }

}
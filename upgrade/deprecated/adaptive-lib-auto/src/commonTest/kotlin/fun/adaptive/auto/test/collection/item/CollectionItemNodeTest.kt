package `fun`.adaptive.auto.test.collection.item

import `fun`.adaptive.adat.api.update
import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.auto.api.autoItemNode
import `fun`.adaptive.auto.model.itemId
import `fun`.adaptive.auto.test.support.AutoTest.Companion.autoTest
import `fun`.adaptive.auto.test.support.TestData
import `fun`.adaptive.auto.test.support.wait
import kotlin.test.Test
import kotlin.test.assertEquals

class CollectionItemNodeTest {

    val td12 = TestData(12, "a")
    val content_12 = listOf(td12)

    @Test
    fun connect_to_a_specific_item() = autoTest {

        val origin = autoCollectionOrigin(content_12, worker = serverWorker)

        val itemId = origin.value.first().itemId

        val node = autoItemNode<TestData>(worker = clientWorker) { origin.connectInfo(itemId = itemId) }

        wait(origin, node)

        val item1 = node.value
        assertEquals(td12, item1)
        item1.update(item1::i to 123)

        wait(origin, node)

        val item2 = origin.value.first()
        assertEquals(123, item2.i)
        item2.update(item2::i to 234)

        wait(origin, node)

        assertEquals(234, node.value.i)
    }

}
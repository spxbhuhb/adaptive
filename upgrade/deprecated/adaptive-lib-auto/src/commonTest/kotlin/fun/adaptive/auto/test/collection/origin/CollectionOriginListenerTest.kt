package `fun`.adaptive.auto.test.collection.origin

import `fun`.adaptive.auto.api.AutoCollectionListener
import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.test.support.TestData
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

class CollectionOriginListenerTest {

    val td12 = TestData(12, "a")
    val td23 = TestData(23, "b")

    val content_empty = listOf<TestData>()
    val content_12 = listOf(td12)
    val content_23 = listOf(td23)
    val content_12_23 = listOf(td12, td23)

    @Test
    fun add_to_empty() {
        val listener = TestListener()
        val instance = autoCollectionOrigin(content_empty, listener = listener)

        instance += td12
        listener.assertLocal(2, td12, null)

        instance += td23
        listener.assertLocal(3, td23, null)
    }

    @Test
    fun add_and_remove() {
        val listener = TestListener()
        val instance = autoCollectionOrigin(content_empty, listener = listener)

        instance += td12
        listener.assertLocal(2, td12, null)

        instance -= instance.value.first()
        listener.assertLocal(2, null, td12)
    }

    @Test
    fun update() {
        val listener = TestListener()
        val instance = autoCollectionOrigin(content_12, listener = listener)

        instance.update(td12::i to 23) { it == td12 }
        listener.assertLocal(1, TestData(23, "a"), TestData(12, "a"))
    }

    class TestListener : AutoCollectionListener<TestData>() {
        val localChanges = mutableListOf<Triple<ItemId, TestData?, TestData?>>()
        val remoteChanges = mutableListOf<Triple<ItemId, TestData?, TestData?>>()

        override fun onLocalChange(itemId: ItemId, newValue: TestData, oldValue: TestData?) {
            localChanges += Triple(itemId, newValue, oldValue)
        }

        override fun onRemoteChange(itemId: ItemId, newValue: TestData, oldValue: TestData?) {
            remoteChanges += Triple(itemId, newValue, oldValue)
        }

        override fun onLocalRemove(itemId: ItemId, removed: TestData?) {
            localChanges += Triple(itemId, null, removed)
        }

        override fun onRemoteRemove(itemId: ItemId, removed: TestData?) {
            remoteChanges += Triple(itemId, null, removed)
        }

        fun assertLocal(time: Long, newValue: TestData?, oldValue: TestData?) {
            assertContentEquals(
                listOf(Triple(LamportTimestamp(0, time), newValue, oldValue)),
                localChanges
            )
            assertTrue(remoteChanges.isEmpty())
            localChanges.clear()
        }

    }
}
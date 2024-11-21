package `fun`.adaptive.auto.test.item.origin

import `fun`.adaptive.auto.api.AutoItemListener
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.test.support.TestData
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertTrue

class ItemOriginListenerTest {

    @Test
    fun listener() {
        val td = TestData(12, "a")
        val listener = TestListener()
        val instance = autoItemOrigin(td, listener = listener)

        instance.update(td::i to 23)
        listener.assertLocal(TestData(23, "a"), TestData(12, "a"))

        instance.update(td::s to "b")
        listener.assertLocal(TestData(23, "b"), TestData(23, "a"))

        instance.update(td::s to "c", td::i to 34)
        listener.assertLocal(TestData(34, "c"), TestData(23, "b"))
    }

    class TestListener : AutoItemListener<TestData>() {
        val localChanges = mutableListOf<Triple<ItemId, TestData, TestData?>>()
        val remoteChanges = mutableListOf<Triple<ItemId, TestData, TestData?>>()

        override fun onLocalChange(itemId: ItemId, newValue: TestData, oldValue: TestData?) {
            localChanges += Triple(itemId, newValue, oldValue)
        }

        override fun onRemoteChange(itemId: ItemId, newValue: TestData, oldValue: TestData?) {
            remoteChanges += Triple(itemId, newValue, oldValue)
        }

        fun assertLocal(newValue: TestData, oldValue: TestData?) {
            assertContentEquals(
                listOf(Triple(ItemId.ORIGIN, newValue, oldValue)),
                localChanges
            )
            assertTrue(remoteChanges.isEmpty())
            localChanges.clear()
        }

    }
}
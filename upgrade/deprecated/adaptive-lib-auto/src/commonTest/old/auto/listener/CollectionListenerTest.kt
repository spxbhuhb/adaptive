package `fun`.adaptive.auto.listener

import `fun`.adaptive.auto.api.AutoCollectionListener
import `fun`.adaptive.auto.api.autoList
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.testing.AutoTestApi
import `fun`.adaptive.auto.testing.AutoTestBase.Companion.autoTestWorker
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.service.api.getService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

@ExperimentalCoroutinesApi
class CollectionListenerTest {

    companion object {
        const val INIT = 1
        const val SYNC_END = 2
        const val CHANGE = 3
        const val REMOVE = 4
    }

    class TestCollectionListener(
        backendOnly : Boolean,
    ) : AutoCollectionListener<TestData>(
        backendOnly
    ) {
        val changes = mutableListOf<Triple<Int, TestData?, TestData?>>()

        override fun onInit(value: List<TestData>) {
            changes += Triple(INIT, null, null)
        }

        override fun onSyncEnd() {
            changes += Triple(SYNC_END, null, null)
        }

        override fun onChange(newValue: TestData, oldValue: TestData?) {
            changes += Triple(CHANGE, newValue, oldValue)
        }

        override fun onRemove(item: TestData) {
            changes += Triple(REMOVE, null, item)
        }

        fun assertCallback(index : Int, type : Int, newValue: TestData?, oldValue: TestData?) {
            assertTrue(index < changes.size)
            assertEquals(
                Triple(type, newValue, oldValue),
                changes[index]
            )
        }
    }

    @Test
    fun basic() = autoTestWorker(trace = true) {

        withContext(Dispatchers.Default.limitedParallelism(1)) {

            val listener = TestCollectionListener(false)

            val modelService = getService<AutoTestApi>(clientTransport)

            val connectInfo = modelService.list()

            assertNotNull(connectInfo)

            val list = autoList(
                clientBackend.firstImpl<AutoWorker>(),
                TestData,
                handle = connectInfo.connectingHandle,
                listener = listener
            )

            listener.assertCallback(0, INIT, null, null)

            list.connect(2.seconds) { connectInfo }

            listener.assertCallback(1, CHANGE, TestData(12, "a"), null)
            listener.assertCallback(2, SYNC_END, null, null)

            val instance = list.frontend.values.first()

            assertEquals(TestData(12, "a"), instance)

            list.update(instance, TestData(13, "b"))

            listener.assertCallback(3, CHANGE, TestData(13, "b"), TestData(12, "a"))

            list.remove { it.i == 13 }

            listener.assertCallback(4, REMOVE, null, TestData(13, "b"))

            val serverList = serverList()
            waitForSync(serverList, list)

            assertTrue(serverList.isEmpty())

            list.add(TestData(24, "c"))

            listener.assertCallback(5, CHANGE, TestData(24, "c"), null)
        }
    }

    @Test
    fun backendOnly() = autoTestWorker {

        withContext(Dispatchers.Default.limitedParallelism(1)) {

            val listener = TestCollectionListener(true)

            val modelService = getService<AutoTestApi>(clientTransport)

            val connectInfo = modelService.list()

            assertNotNull(connectInfo)

            val list = autoList(
                clientBackend.firstImpl<AutoWorker>(),
                TestData,
                handle = connectInfo.connectingHandle,
                listener = listener,
                trace = true
            )

            // skipped by backend only
            // listener.assertCallback(0, INIT, null, null)
            assertEquals(0, listener.changes.size)

            list.connect(2.seconds) { connectInfo }

            listener.assertCallback(0, CHANGE, TestData(12, "a"), null)
            listener.assertCallback(1, SYNC_END, null, null)

            val instance = list.frontend.values.first()

            assertEquals(TestData(12, "a"), instance)

            list.update(instance, TestData(13, "b"))

            // skipped by backend only
            // listener.assertCallback(3, CHANGE, TestData(13, "b"), TestData(12, "a"))
            assertEquals(2, listener.changes.size)

            list.remove { it.i == 13 }

            // skipped by backend only
            // listener.assertCallback(4, REMOVE, null, TestData(13, "b"))
            assertEquals(2, listener.changes.size)

            list.add(TestData(24, "c"))

            // skipped by backend only
            // listener.assertCallback(5, CHANGE, TestData(24, "c"), null)
            assertEquals(2, listener.changes.size)

            val serverList = serverList()

            serverList.add(TestData(45, "d"))
            waitForSync(serverList, list)

            listener.assertCallback(2, CHANGE, TestData(45, "d"), null)

            serverList.update(serverList.values.first(), TestData(56, "e"))
            waitForSync(serverList, list)

            listener.assertCallback(3, CHANGE, TestData(56, "e"), TestData(45, "d"))

            serverList.remove { it.i == 56 }
            waitForSync(serverList, list)

            listener.assertCallback(4, REMOVE, null, TestData(56, "e"))
        }
    }
}

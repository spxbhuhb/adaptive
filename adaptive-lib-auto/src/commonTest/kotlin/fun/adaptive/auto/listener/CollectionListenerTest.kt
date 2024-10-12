package `fun`.adaptive.auto.listener

import `fun`.adaptive.auto.api.AutoCollectionListener
import `fun`.adaptive.auto.api.autoList
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.testing.AutoTestApi
import `fun`.adaptive.auto.testing.AutoTestBase.Companion.autoTestWorker
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.service.getService
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

    object TestCollectionListener : AutoCollectionListener<TestData>() {
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
    }

    @Test
    fun basic() = autoTestWorker {

        withContext(Dispatchers.Default.limitedParallelism(1)) {

            val modelService = getService<AutoTestApi>(clientTransport)

            val connectInfo = modelService.list()

            assertNotNull(connectInfo)

            val list = autoList(
                clientBackend.firstImpl<AutoWorker>(),
                TestData,
                handle = connectInfo.connectingHandle,
                listener = TestCollectionListener
            )

            assertCallback(0, INIT, null, null)

            list.connect(2.seconds) { connectInfo }

            assertCallback(1, CHANGE, TestData(12, "a"), null)
            assertCallback(2, SYNC_END, null, null)

            val instance = list.frontend.values.first()

            assertEquals(TestData(12, "a"), instance)

            list.update(instance, TestData(13, "b"))

            assertCallback(3, CHANGE, TestData(13, "b"), TestData(12, "a"))

            list.remove { it.i == 13 }

            assertCallback(4, REMOVE, null, TestData(13, "b"))
        }
    }

    fun assertCallback(index : Int, type : Int, newValue: TestData?, oldValue: TestData?) {
        assertTrue(index < TestCollectionListener.changes.size)
        assertEquals(
            Triple(type, newValue, oldValue),
            TestCollectionListener.changes[index]
        )
    }
}

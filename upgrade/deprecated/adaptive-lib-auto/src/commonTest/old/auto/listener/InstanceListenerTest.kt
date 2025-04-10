package `fun`.adaptive.auto.listener

import `fun`.adaptive.auto.api.AutoItemListener
import `fun`.adaptive.auto.api.autoItem
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
import kotlin.time.Duration.Companion.seconds

@ExperimentalCoroutinesApi
class InstanceListenerTest {

    object TestItemListener : AutoItemListener<TestData>() {
        val changes = mutableListOf<Pair<TestData, TestData?>>()

        override fun onChange(newValue: TestData, oldValue: TestData?) {
            changes += newValue to oldValue
        }
    }

    @Test
    fun basic() = autoTestWorker {

        withContext(Dispatchers.Default.limitedParallelism(1)) {

            val modelService = getService<AutoTestApi>(clientTransport)

            val connectInfo = modelService.instance()

            assertNotNull(connectInfo)

            val instance = autoItem(
                clientBackend.firstImpl<AutoWorker>(),
                TestData,
                handle = connectInfo.connectingHandle,
                listener = TestItemListener
            )

            // not connected yet
            assertEquals(0, TestItemListener.changes.size)

            instance.connect(2.seconds) { connectInfo }

            assertEquals(TestData(12, "a"), instance.frontend.value)

            assertEquals(1, TestItemListener.changes.size)

            assertEquals(
                TestData(12, "a") to null,
                TestItemListener.changes.first()
            )

            instance.update(TestData(13, "b"))

            assertEquals(2, TestItemListener.changes.size)

            assertEquals(
                TestData(13, "b") to TestData(12, "a"),
                TestItemListener.changes.last()
            )
        }
    }

}

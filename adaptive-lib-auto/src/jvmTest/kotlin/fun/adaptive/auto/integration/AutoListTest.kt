package `fun`.adaptive.auto.integration

import `fun`.adaptive.auto.api.autoList
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.service.getService
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals

private var producedValue: List<TestData>? = null

class AutoListTest {

    @Test
    fun basic() {
        autoTest(port = 8085) { originAdapter, connectingAdapter ->

            val testAdapter = test(connectingAdapter) {
                val a = autoList(TestData, trace = true) { getService<AutoTestApi>().testListWithOrigin() }

                if (a != null) {
                    producedValue = a
                }
            }

            withTimeout(1000L) {
                while (true) {
                    if (testAdapter.rootFragment.state[0] != null) break
                    delay(10)
                }
            }

            val originWorker = originAdapter.firstImpl<AutoWorker>()
            val originBackend = originWorker.backends.values.first()

            val connectingWorker = connectingAdapter.firstImpl<AutoWorker>()
            val connectingBackend = connectingWorker.backends.values.first()

            @Suppress("UNCHECKED_CAST")
            val originFrontend = originBackend.frontEnd as AdatClassListFrontend<TestData>

            val t1 = TestData(12, "ab")

            originFrontend.add(t1)

            waitForSync(originWorker, originBackend.context.handle, connectingWorker, connectingBackend.context.handle)

            assertEquals(t1, producedValue!!.first())
        }
    }
}
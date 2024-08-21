package `fun`.adaptive.auto.integration

import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.worker.AutoWorker
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.service.getService
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals

var producedValue: TestData? = null

class AutoInstanceTest {

    @Test
    fun basic() {
        autoTest(port = 8084) { originAdapter, connectingAdapter ->

            val testAdapter = test(connectingAdapter) {
                val a = autoInstance<TestData> { getService<AutoTestApi>().testInstanceWithOrigin() }

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

            val instance = testAdapter.rootFragment.state[0]

            assertEquals(TestData(12, "a"), instance)
            assertEquals(TestData(12, "a"), producedValue)

            val originBackend = originAdapter.firstImpl<AutoWorker>().backends.values.first()
            assertEquals(1, originBackend.context.connectors.size)

            testAdapter.rootFragment.dispose()

            withTimeout(1000L) {
                while (true) {
                    if (originBackend.context.connectors.isEmpty()) break
                    delay(10)
                }
            }
        }
    }
}
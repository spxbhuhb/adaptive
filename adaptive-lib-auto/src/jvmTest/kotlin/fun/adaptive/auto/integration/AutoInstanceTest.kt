package `fun`.adaptive.auto.integration

import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.worker.AutoWorker
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.service.getService
import junit.framework.TestCase.assertTrue
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
                val a = autoInstance<TestData>(trace = true) { getService<AutoTestApi>().testInstanceWithOrigin() }

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

            testAdapter.rootFragment.dispose()

            val backend = originAdapter.firstImpl<AutoWorker>().backends.values.first()
            assertTrue(backend.context.connectors.isEmpty())
        }
    }
}
package `fun`.adaptive.auto.integration

import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.auto.backend.TestData
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
        autoTest(port = 8081) { originAdapter, connectingAdapter ->
            val testAdapter = test(connectingAdapter) {
                val a = autoInstance<TestData> { getService<AutoTestApi>().testInstance() }
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
        }
    }
}
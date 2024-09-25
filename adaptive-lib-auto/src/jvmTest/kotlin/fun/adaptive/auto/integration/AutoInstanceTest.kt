package `fun`.adaptive.auto.integration

import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.internal.frontend.FileFrontend
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.service.getService
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.utility.exists
import `fun`.adaptive.utility.testPath
import `fun`.adaptive.utility.waitForReal
import junit.framework.TestCase.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.seconds

private var producedValue: TestData = TestData(0, "")

class AutoInstanceTest {

    @Test
    fun basic() {
        testPath.ensure()

        autoTest(port = 8085) { originAdapter, connectingAdapter ->

            val testAdapter = test(connectingAdapter) {

                val a = autoInstance<TestData>(trace = true) { getService<AutoTestApi>(connectingAdapter.transport).file() }

                if (a != null) {
                    producedValue = a
                }
            }

            waitForReal(2.seconds) { testAdapter.rootFragment.state[0] != null }

            val originBackend = originAdapter.firstImpl<AutoWorker>().backends.values.first()
            assertEquals(1, originBackend.context.connectors.size)

            val originFrontend = originBackend.frontend
            assertIs<FileFrontend<*>>(originFrontend)

            assertTrue(originFrontend.path.exists())

            val instance = testAdapter.rootFragment.state[0]

            assertEquals(TestData(12, "a"), instance)
            assertEquals(TestData(12, "a"), producedValue)

            testAdapter.rootFragment.dispose()

            waitForReal(2.seconds) { originBackend.context.connectors.isEmpty() }

        }
    }
}
package `fun`.adaptive.auto.integration

import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.internal.frontend.FileFrontend
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.foundation.testing.AdaptiveTestAdapter
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.service.getService
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.utility.exists
import `fun`.adaptive.utility.testPath
import `fun`.adaptive.utility.waitForReal
import `fun`.adaptive.wireformat.api.Json
import kotlinx.io.files.SystemFileSystem
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

private var producedValue: TestData? = null

class FileInstanceTest {

    @Test
    fun basic() {
        testPath.ensure()

        autoTest(port = 8085) { originAdapter, connectingAdapter ->

            val testAdapter = test(connectingAdapter) {
                val a = autoInstance<TestData> { getService<AutoTestApi>(connectingAdapter.transport).file() }

                if (a != null) {
                    producedValue = a
                }
            }

            waitForReal(2.seconds) { (testAdapter.rootFragment.state[0] as? TestData)?.i == 12 }

            val originBackend = originAdapter.firstImpl<AutoWorker>().backends.values.first()

            @Suppress("UNCHECKED_CAST")
            val frontend = originBackend.frontend as FileFrontend<TestData>

            // ---- Initial Sync ----

            assert(TestData(12, "a"), testAdapter, frontend)

            // ---- Hackish update ----

            val instance = testAdapter.rootFragment.state[0] as TestData
            instance.adatContext !!.store !!.update(instance, arrayOf("i"), 23)

            // we can delete the file safely here
            SystemFileSystem.delete(frontend.path)

            waitForReal(2.seconds) { frontend.path.exists() }

            assert(TestData(23, "a"), testAdapter, frontend)

            // ---- Shutdown ----

            assertEquals(1, originBackend.context.connectors.size)

            testAdapter.rootFragment.dispose()

            waitForReal(2.seconds) { originBackend.context.connectors.isEmpty() }

        }
    }

    fun assert(expected: TestData, adapter: AdaptiveTestAdapter, frontend: FileFrontend<*>) {
        val instance = adapter.rootFragment.state[0]

        val fromFile = FileFrontend.read<TestData>(frontend.path, Json).third

        assertEquals(expected, instance)
        assertEquals(expected, producedValue)
        assertEquals(expected, fromFile)
    }
}
package `fun`.adaptive.auto.basic

import `fun`.adaptive.auto.api.autoInstance
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
import kotlin.time.Duration.Companion.seconds

@ExperimentalCoroutinesApi
class ItemConnectTest {

    @Test
    fun basic() = autoTestWorker(true) {

        withContext(Dispatchers.Default.limitedParallelism(1)) {

            val autoTestService = getService<AutoTestApi>(clientTransport)

            val connectInfo = autoTestService.item(12)

            assertNotNull(connectInfo)
            assertNotNull(connectInfo.connectingHandle.itemId)

            val instance = autoInstance(
                clientBackend.firstImpl<AutoWorker>(),
                TestData,
                handle = connectInfo.connectingHandle,
                trace = true
            )

            instance.connect(2.seconds) { connectInfo }

            assertEquals(TestData(12, "a"), instance.frontend.value)
        }

    }

}

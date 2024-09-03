package `fun`.adaptive.auto.integration

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.api.autoPolyList
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.service.getService
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import org.junit.Test
import kotlin.test.assertEquals

private var producedValue: List<AdatClass<*>>? = null

@Adat
private class TD2(
    val i2: Int
)

@Adat
private class TD3(
    val i3: Int
)

class AutoPolyListTest {

    @Test
    fun basic() {
        autoTest(port = 8085) { originAdapter, connectingAdapter ->

            val testAdapter = test(connectingAdapter) {

                val a = autoPolyList(TestData) { getService<AutoTestApi>().testPolyListWithOrigin() }

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
            val originFrontend = originBackend.frontEnd as AdatClassListFrontend<AdatClass<*>>

            val t1 = TestData(12, "ab")
            val t2 = TD2(23)
            val t3 = TD3(34)

            originFrontend.add(t1)
            originFrontend.add(t2)
            originFrontend.add(t3)

            waitForSync(originWorker, originBackend.context.handle, connectingWorker, connectingBackend.context.handle)

            assertEquals(t1, producedValue !!.first())
        }
    }
}
package `fun`.adaptive.auto.integration

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.api.autoListPoly
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.service.getService
import `fun`.adaptive.utility.waitForReal
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

private var producedValue: List<AdatClass>? = null

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

                val a = autoListPoly(TestData) { getService<AutoTestApi>().polyList() }

                if (a != null) {
                    producedValue = a
                }
            }

            waitForReal(2.seconds) { testAdapter.rootFragment.state[0] != null }

            val originWorker = originAdapter.firstImpl<AutoWorker>()
            val originBackend = originWorker.backends.values.first()

            val connectingWorker = connectingAdapter.firstImpl<AutoWorker>()
            val connectingBackend = connectingWorker.backends.values.first()

            @Suppress("UNCHECKED_CAST")
            val originFrontend = originBackend.frontEnd as AdatClassListFrontend<AdatClass>

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
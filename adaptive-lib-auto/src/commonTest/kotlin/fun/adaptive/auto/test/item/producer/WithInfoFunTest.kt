package `fun`.adaptive.auto.test.item.producer

import `fun`.adaptive.adat.api.update
import `fun`.adaptive.auto.api.ItemBase
import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.test.support.AutoTest.Companion.autoTest
import `fun`.adaptive.auto.test.support.TestData
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.utility.waitFor
import `fun`.adaptive.utility.waitForReal
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

private val td = TestData(12, "a")

// FIXME function local variables are not handled by the compiler plugin
private lateinit var infoFunBasicOrigin: ItemBase<TestData>
private lateinit var infoFunUpdateOrigin: ItemBase<TestData>

class WithInfoFunTest {

    @Test
    fun basic() = autoTest {

        infoFunBasicOrigin = autoItemOrigin(td, worker = serverBackend.firstImpl<AutoWorker>())

        val adapter = test(clientBackend) {
            @Suppress("UNUSED_VARIABLE", "unused")
            val item = autoItem { infoFunBasicOrigin.connectInfo() }
        }

        @Suppress("UNCHECKED_CAST")
        fun item() = (adapter.rootFragment.get<TestData?>(1))

        waitFor(1.seconds) { item() != null }

        // I'm not 100% sure about the timing here, values may contain
        // nulls as well depending on how the coroutines run. Using
        // `last` should take care about that problem.

        infoFunBasicOrigin.update(td::i to 23)

        waitFor(1.seconds) { item()?.i == 23 }
    }

    @Test
    fun adaptiveUpdate() = autoTest {

        // see comments in `basic` and in `WithInstanceTest`

        var i = 0
        while (i ++ < 5) {
            infoFunUpdateOrigin = autoItemOrigin(td, worker = serverBackend.firstImpl<AutoWorker>())

            val adapter = test(clientBackend) {
                val item = autoItem { infoFunUpdateOrigin.connectInfo() }
                if (item?.i == 23) {
                    item.update(item::i, 34)
                }
            }

            @Suppress("UNCHECKED_CAST")
            fun item() = (adapter.rootFragment.get<TestData?>(1))

            waitForReal(1.seconds) { item() != null }

            infoFunUpdateOrigin.update(td::i to 23)

            waitForReal(1.seconds) { item()?.i == 23 || item()?.i == 34 }

            waitForReal(1.seconds) { item()?.i == 34 }
        }
    }

}
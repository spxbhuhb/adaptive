package `fun`.adaptive.auto.test.item.producer

import `fun`.adaptive.adat.api.update
import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.auto.test.support.AutoTest.Companion.autoTest
import `fun`.adaptive.auto.test.support.TestData
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import `fun`.adaptive.utility.waitForReal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds


class WithInstanceTest {

    @Test
    fun basic() = autoTest {

        // In this case the producer is simply a listener on origin.
        // The value is immediately set by `onInit`. `onInit` runs in
        // a coroutine, so we have to give the test the opportunity
        // to execute it, hence `delay`.

        val td = TestData(12, "a")
        val origin = autoItemOrigin(td)

        val adapter = test {
            @Suppress("UNUSED_VARIABLE", "unused")
            val item = autoItem(origin)
        }

        @Suppress("UNCHECKED_CAST")
        fun item() = (adapter.rootFragment.state[0] as TestData?)

        waitForReal(1.seconds) { item() != null }

        // I'm not 100% sure about the timing here, values may contain
        // nulls as well depending on how the coroutines run. Using
        // `last` should take care about that problem.

        origin.update(td::i to 23)

        waitForReal(1.seconds) { item()?.i == 23 }
    }

    @Test
    fun adaptiveUpdate() = autoTest {

        var i = 0
        while (i ++ < 5) {
            // see comments in `basic`

            val td = TestData(12, "a")
            val origin = autoItemOrigin(td)

            val adapter = test(clientBackend, printTrace = true) {
                val item = autoItem(origin)
                if (item?.i == 23) {
                    item.update(item::i, 34)
                }
            }

            @Suppress("UNCHECKED_CAST")
            fun item() = (adapter.rootFragment.state[0] as TestData?)

            waitForReal(1.seconds) { item() != null }

            origin.update(td::i to 23)

            waitForReal(1.seconds) { item()?.i == 23 || item()?.i == 34 }

            waitForReal(1.seconds) { item()?.i == 34 }
        }
    }

}
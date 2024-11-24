package `fun`.adaptive.auto.test.item.producer

import `fun`.adaptive.adat.api.update
import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.auto.test.support.AutoTest.Companion.autoTest
import `fun`.adaptive.auto.test.support.TestData
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.utility.waitForReal
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

private val td = TestData(12, "a")

// TODO move these into the test functions when the compiler plugin supports it

private val basicOrigin = autoItemOrigin(td)
private val basicValues = mutableListOf<TestData?>()

private val updateOrigin = autoItemOrigin(td, trace = true)
private val updateValues = mutableListOf<TestData?>()

class WithInstanceTest {

    @Test
    fun basic() = autoTest {

        // In this case the producer is simply a listener on origin.
        // The value is immediately set by `onInit`. `onInit` runs in
        // a coroutine, so we have to give the test the opportunity
        // to execute it, hence `delay`.

        test {
            val item = autoItem(basicOrigin)
            basicValues += item
        }

        waitForReal(1.seconds) { basicValues.isNotEmpty() }

        // I'm not 100% sure about the timing here, values may contain
        // nulls as well depending on how the coroutines run. Using
        // `last` should take care about that problem.

        basicOrigin.update(td::i to 23)
        waitForReal(1.seconds) { basicValues.last()?.i == 23 }
    }

    @Test
    fun adaptiveUpdate() = autoTest {

        // see comments in `basic`

        // In this test it is important for the client backend to run in the
        // test scope, just as it would be important to run all UI updates in
        // the UI thread. If it runs in different scope, the update run by
        // during patching (the update to 34) may run before the patching for
        // 23 ends and that results in an invalid state.

        test(clientBackend) {
            val item = autoItem(updateOrigin)
            updateValues += item
            if (item?.i == 23) {
                item.update(item::i, 34)
            }
        }

        waitForReal(1.seconds) { updateValues.isNotEmpty() }

        updateOrigin.update(td::i to 23)

        // This is tricky:
        //
        // 1. The update sets `i` to 23 and calls `onLocalChange` of the producer.
        // 2. The producer launches a coroutine which patches the state.
        // 3. Patching the state sets `i` to 34 and calls `onLocalChange` of the producer.
        // 4. The producer launches a coroutine which patches the state.
        // 5. Patching the state saves 34 into values.

        waitForReal(1.seconds) { updateValues.last()?.i == 34 }
    }

}
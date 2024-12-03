package `fun`.adaptive.auto.test.collection.producer

import `fun`.adaptive.adat.api.update
import `fun`.adaptive.auto.api.autoCollection
import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.auto.test.support.AutoTest.Companion.autoTest
import `fun`.adaptive.auto.test.support.TestData
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.utility.waitFor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class WithInfoFunTest {

    val td12 = TestData(12, "a")
    val td23 = TestData(23, "b")
    val td45 = TestData(45, "c")

    val content_empty = listOf<TestData>()
    val content_12 = listOf(td12)
    val content_23 = listOf(td23)
    val content_12_23 = listOf(td12, td23)
    val content_45_23 = listOf(td45, td23)

    @Test
    fun basic() = autoTest {

        val origin = autoCollectionOrigin(content_12_23, serverWorker)

        val adapter = test(clientBackend) {
            @Suppress("UNUSED_VARIABLE", "unused")
            val collection = autoCollection { origin.connectInfo() }
        }

        @Suppress("UNCHECKED_CAST")
        fun collection() = (adapter.rootFragment.state[0] as Collection<TestData>?)

        waitFor(1.seconds) { collection() != null }

        // I'm not 100% sure about the timing here, values may contain
        // nulls as well depending on how the coroutines run. Using
        // `last` should take care about that problem.

        val item = collection()!!.first()
        assertEquals(td12, item)
        item.update(item::i to 45)

        waitFor(1.seconds) { collection()!!.first().i == 45 }
    }


}
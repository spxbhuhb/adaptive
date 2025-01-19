package `fun`.adaptive.auto.test.collection.producer

import `fun`.adaptive.adat.api.update
import `fun`.adaptive.auto.api.CollectionBase
import `fun`.adaptive.auto.api.autoCollection
import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.auto.test.support.AutoTest.Companion.autoTest
import `fun`.adaptive.auto.test.support.TestData
import `fun`.adaptive.auto.test.support.content_12_23
import `fun`.adaptive.auto.test.support.td12
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.utility.waitFor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

private lateinit var origin: CollectionBase<TestData>

// FIXME function local variables are not handled by the compiler plugin
class WithInfoFunTest {

    @Test
    fun basic() = autoTest {

        origin = autoCollectionOrigin(content_12_23, serverWorker)

        val adapter = test(clientBackend) {
            @Suppress("UNUSED_VARIABLE", "unused")
            val collection = autoCollection { origin.connectInfo() }
        }

        @Suppress("UNCHECKED_CAST")
        fun collection() = (adapter.rootFragment.get<Collection<TestData>?>(0))

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
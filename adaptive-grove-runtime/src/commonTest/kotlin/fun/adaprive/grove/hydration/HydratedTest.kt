package `fun`.adaprive.grove.hydration

import `fun`.adaptive.foundation.query.first
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.grove.api.GroveFragmentFactory
import `fun`.adaptive.grove.api.hydrated
import `fun`.adaptive.grove.hydration.GroveHydrated
import `fun`.adaptive.grove.hydration.lfm.LfmFragment
import kotlin.test.Test

class HydratedTest {

    @Test
    fun empty() {
        val lfm = LfmFragment(emptyList(), emptyList(), emptyList())

        test {
            it.fragmentFactory += arrayOf(GroveFragmentFactory)
            hydrated(lfm)

        }.first<GroveHydrated>().apply {
            state.size shouldBe 1
            model shouldBe lfm
        }
    }

}

package `fun`.adaptive.grove.api

import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.grove.grove
import `fun`.adaptive.grove.hydration.model.AfmFragment

@AdaptiveExpect(grove)
fun hydrated(source : AfmFragment, vararg externalState : Any?) : AdaptiveFragment {
    manualImplementation(source, externalState)
}
package `fun`.adaptive.grove.api

import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.grove.grove
import `fun`.adaptive.grove.hydration.model.DehydratedFragment

@AdaptiveExpect(grove)
fun hydrated(source : DehydratedFragment) : AdaptiveFragment {
    manualImplementation()
}
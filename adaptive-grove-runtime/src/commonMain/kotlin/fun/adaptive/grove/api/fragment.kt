package `fun`.adaptive.grove.api

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.AdaptiveHydrated
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.grove.grove
import `fun`.adaptive.grove.hydration.lfm.LfmFragment

@AdaptiveHydrated(grove)
fun hydrated(source : LfmFragment, vararg externalStateVariables : Any?) : AdaptiveFragment {
    manualImplementation(source, externalStateVariables)
}
package `fun`.adaptive.grove.api

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.grove.hydration.GroveHydrated

object GroveRuntimeFragmentFactory : FoundationFragmentFactory() {
    init {
        add("grove:hydrated") { p, i, s -> GroveHydrated(p.adapter, p, i, s) }
    }
}
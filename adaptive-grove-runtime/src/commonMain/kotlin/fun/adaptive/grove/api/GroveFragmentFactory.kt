package `fun`.adaptive.grove.api

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.grove.hydration.GroveHydrated

object GroveFragmentFactory : FoundationFragmentFactory() {
    init {
        add("grove:hydrated") { p, i, s -> GroveHydrated(p.adapter, p, i, s) }
    }
}
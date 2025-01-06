package `fun`.adaptive.grove.api

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.grove.hydration.HydratedFragment

object GroveFragmentFactory : FoundationFragmentFactory() {
    init {
        add("grove:hydrated") { p, i -> HydratedFragment(p.adapter, p, i) }
    }
}
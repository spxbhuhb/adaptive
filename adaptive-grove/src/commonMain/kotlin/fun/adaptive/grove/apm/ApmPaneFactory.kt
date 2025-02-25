package `fun`.adaptive.grove.apm

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object ApmPaneFactory : FoundationFragmentFactory() {
    init {
        add("grove:apm:project", ::apmProject)
    }
}
package `fun`.adaptive.grove.apm

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object ApmPaneFactory : FoundationFragmentFactory() {
    init {
        add(ApmWsContext.APM_PROJECT_TOOL_KEY, ::apmProject)
    }
}
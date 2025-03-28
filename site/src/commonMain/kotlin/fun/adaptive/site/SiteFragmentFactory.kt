package `fun`.adaptive.site

import `fun`.adaptive.app.ws.BasicAppWsModule
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object SiteFragmentFactory : FoundationFragmentFactory() {
    init {
        add(BasicAppWsModule.HOME_CONTENT_KEY, ::siteHome)
    }
}
package `fun`.adaptive.site

import `fun`.adaptive.app.ws.AppMainWsModule
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object SiteFragmentFactory : FoundationFragmentFactory() {
    init {
        add(AppMainWsModule.HOME_CONTENT_KEY, ::siteHome)
    }
}
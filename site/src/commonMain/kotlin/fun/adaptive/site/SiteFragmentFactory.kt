package `fun`.adaptive.site

import `fun`.adaptive.app.ws.WsAppModule
import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object SiteFragmentFactory : FoundationFragmentFactory() {
    init {
        add(WsAppModule.HOME_CONTENT_KEY, ::siteHome)
    }
}
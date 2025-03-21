package `fun`.adaptive.site

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.ui.app.ws.appHomeKey

object SiteFragmentFactory : FoundationFragmentFactory() {
    init {
        add(appHomeKey, ::siteHome)
    }
}
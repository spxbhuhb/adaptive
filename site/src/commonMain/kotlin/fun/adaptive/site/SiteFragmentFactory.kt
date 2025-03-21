package `fun`.adaptive.site

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.site.siteHome
import `fun`.adaptive.ui.app.appHomeKey

object SiteFragmentFactory : FoundationFragmentFactory() {
    init {
        add(appHomeKey, ::siteHome)
    }
}
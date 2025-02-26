package `fun`.adaptive.site

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.site.siteHome

object SiteFragmentFactory : FoundationFragmentFactory() {
    init {
        add("site:home", ::siteHome)
    }
}
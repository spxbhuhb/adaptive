import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory

object SiteFragmentFactory : FoundationFragmentFactory() {
    init {
        add("site:home", ::siteHome)
    }
}
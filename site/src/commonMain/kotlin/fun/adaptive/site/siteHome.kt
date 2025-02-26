package `fun`.adaptive.site

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.text


@Adaptive
fun siteHome(): AdaptiveFragment {
    text("site")
    return fragment()
}
package `fun`.adaptive.cookbook.auth.ui.responsive

import `fun`.adaptive.cookbook.auth.ui.large.largeSignIn
import `fun`.adaptive.cookbook.auth.ui.small.smallSignIn
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.mediaMetrics

@Adaptive
fun signIn(): AdaptiveFragment {
    val media = mediaMetrics()

    when {
        media.isSmall -> smallSignIn()
        media.isMedium -> largeSignIn()
        media.isLarge -> largeSignIn()
    }

    return fragment()
}
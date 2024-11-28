package `fun`.adaptive.cookbook.auth.ui.responsibe

import `fun`.adaptive.cookbook.auth.ui.large.largeSignIn
import `fun`.adaptive.cookbook.auth.ui.small.smallSignIn
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.mediaMetrics

@Adaptive
fun signIn() {
    val media = mediaMetrics()

    when {
        media.isSmall -> smallSignIn()
        media.isMedium -> smallSignIn()
        media.isLarge -> largeSignIn()
    }

}

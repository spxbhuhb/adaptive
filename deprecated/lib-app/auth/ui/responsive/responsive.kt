package `fun`.adaptive.app.basic.auth.ui.responsive

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.mediaMetrics

@Adaptive
fun responsive(
    @Adaptive _KT_74337_small : () -> Unit,
    @Adaptive _KT_74337_medium : () -> Unit,
    @Adaptive _KT_74337_large : () -> Unit,
): AdaptiveFragment {

    val media = mediaMetrics()

    when {
        media.isSmall -> _KT_74337_small()
        media.isMedium -> _KT_74337_medium()
        media.isLarge -> _KT_74337_large()
    }

    return fragment()
}
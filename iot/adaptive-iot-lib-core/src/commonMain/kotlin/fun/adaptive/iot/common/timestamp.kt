package `fun`.adaptive.iot.common

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.text
import kotlinx.datetime.Instant

@Adaptive
fun timestamp(instant: Instant) : AdaptiveFragment {
    text(instant.localizedString(), instructions())
    return fragment()
}
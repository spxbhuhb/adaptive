package `fun`.adaptive.iot.common

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.text
import kotlinx.datetime.Instant

@Adaptive
fun timestamp(instant: Instant) {
    text(instant.localizedString())
}
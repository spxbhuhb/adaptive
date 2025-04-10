package `fun`.adaptive.iot.common

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.utility.format

@Adaptive
fun relativeHumidity(rh: Double?, decimals: Int = 1) {
    text(rh?.let { "${it.format(decimals)} %" }) .. maxWidth
}
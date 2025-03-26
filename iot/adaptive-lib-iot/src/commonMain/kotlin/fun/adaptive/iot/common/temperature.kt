package `fun`.adaptive.iot.common

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.utility.format

@Adaptive
fun temperature(temperature: Double?, decimals : Int = 1) {
    text(temperature?.let { "${it.format(decimals)} °C" })
}
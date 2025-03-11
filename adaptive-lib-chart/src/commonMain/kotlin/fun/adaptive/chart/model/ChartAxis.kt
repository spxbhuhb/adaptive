package `fun`.adaptive.chart.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.FragmentKey

@Adat
class ChartAxis(
    val size : Double,
    val offset: Double,
    val axisLine: Boolean,
    val renderer: FragmentKey
)
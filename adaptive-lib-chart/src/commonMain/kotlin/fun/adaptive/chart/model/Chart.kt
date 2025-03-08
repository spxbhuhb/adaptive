package `fun`.adaptive.chart.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.FragmentKey

@Adat
class Chart(
    val axes: List<ChartAxis>,
    val series: List<ChartSeries>,
    val renderer: FragmentKey? = null
)
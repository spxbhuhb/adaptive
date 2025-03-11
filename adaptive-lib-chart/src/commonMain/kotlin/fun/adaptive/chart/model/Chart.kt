package `fun`.adaptive.chart.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.FragmentKey

@Adat
class Chart(
    val axes: List<ChartRenderAxis<*,*>>,
    val series: List<ChartRenderSeries>,
    val renderer: FragmentKey? = null
)
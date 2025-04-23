package `fun`.adaptive.app.ws

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.sandbox.recipe.chart.lineChart

@Adaptive
fun appHome(): AdaptiveFragment {
    lineChart()
    return fragment()
}
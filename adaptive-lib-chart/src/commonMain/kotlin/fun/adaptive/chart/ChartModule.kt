package `fun`.adaptive.chart

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.runtime.AppModule

open class ChartModule<WT> : AppModule<WT>() {

    override fun AdaptiveAdapter.init() {
        fragmentFactory += ChartFragmentFactory
    }

}
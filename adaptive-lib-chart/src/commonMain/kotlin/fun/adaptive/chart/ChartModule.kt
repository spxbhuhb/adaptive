package `fun`.adaptive.chart

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.runtime.AppModule

open class ChartModule<WT, AT : Any> : AppModule<WT, AT>() {

    override fun AdaptiveAdapter.init() {
        fragmentFactory += ChartFragmentFactory
    }

}
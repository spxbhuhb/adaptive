package `fun`.adaptive.chart

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule

open class ChartModule<WT : AbstractWorkspace> : AppModule<WT>() {

    override fun frontendAdapterInit(adapter : AdaptiveAdapter)= with(adapter) {
        + ChartFragmentFactory
    }

}
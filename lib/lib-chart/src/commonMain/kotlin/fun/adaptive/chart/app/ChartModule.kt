package `fun`.adaptive.chart.app

import `fun`.adaptive.chart.ui.basicHorizontalAxis
import `fun`.adaptive.chart.ui.basicLineSeries
import `fun`.adaptive.chart.ui.basicVerticalAxis
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule

open class ChartModule<FW : AbstractWorkspace, BW : AbstractWorkspace> : AppModule<FW, BW>() {

    companion object {
        const val BASIC_HORIZONTAL_AXIS = "chart:axis:horizontal:basic"
        const val BASIC_VERTICAL_AXIS = "chart:axis:vertical:basic"
        const val BASIC_LINE_SERIES = "chart:series:line:basic"
    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(BASIC_HORIZONTAL_AXIS, ::basicHorizontalAxis)
        add(BASIC_VERTICAL_AXIS, ::basicVerticalAxis)
        add(BASIC_LINE_SERIES, ::basicLineSeries)
    }

}
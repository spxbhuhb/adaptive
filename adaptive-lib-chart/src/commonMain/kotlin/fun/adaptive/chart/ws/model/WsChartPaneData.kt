package `fun`.adaptive.chart.ws.model

import `fun`.adaptive.foundation.value.storeFor

/**
 * The data for one chart series pane. Works in tandem with
 * [WsChartContext].
 */
class WsChartPaneData {
    val items = storeFor { listOf<WsItemChartSeries>() }
}
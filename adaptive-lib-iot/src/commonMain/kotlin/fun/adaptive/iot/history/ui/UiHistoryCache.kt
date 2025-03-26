package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.chart.model.ChartItem
import `fun`.adaptive.graphics.canvas.model.path.LineTo
import `fun`.adaptive.iot.history.model.AioDoubleHistoryRecord
import `fun`.adaptive.value.item.AvItem
import kotlinx.datetime.Instant

/**
 * Contains history series to display on a chart or in a table.
 */
class UiHistoryCache {

    class CacheItem(
        val point: AvItem<*>,
        var records: List<AioDoubleHistoryRecord> = emptyList(),
    ) {
        val tableItem: TableItem<Double>? = null
        val chartItem: ChartItem<Instant, Double>? = null
    }

    class TableItem<T>(
        val sourceData: List<T>
    ) {
        val normalizedData: List<T> = mutableListOf<T>()

        fun normalize(from: T, to: T) {

        }
    }

//    val chartContext = ChartRenderContext<Instant, Double>(
//        chartItems,
//        listOf(xAxis, yAxis),
//        50.0,
//        50.0,
//        { InstantDoubleNormalizer(it) }
//    )

    val chartData = mutableMapOf<AvItem<*>, CacheItem>()
}
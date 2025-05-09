package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.chart.model.ChartItem
import `fun`.adaptive.iot.history.model.AioDoubleHistoryRecord
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValue
import kotlinx.datetime.Instant

typealias HistoryTreeModel = TreeViewModel<AvValueId, HistoryToolController>
typealias HistoryChartItem = ChartItem<Instant, AioDoubleHistoryRecord, AvValue<*>>
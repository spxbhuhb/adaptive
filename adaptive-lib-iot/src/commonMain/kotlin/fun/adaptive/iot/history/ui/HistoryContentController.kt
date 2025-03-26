package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.adaptive_lib_iot.generated.resources.monitoring
import `fun`.adaptive.chart.model.AbstractChartDataPoint
import `fun`.adaptive.chart.model.ChartItem
import `fun`.adaptive.foundation.value.adaptiveStoreFor
import `fun`.adaptive.iot.history.AioHistoryApi
import `fun`.adaptive.iot.history.model.AioDoubleHistoryRecord
import `fun`.adaptive.iot.history.model.AioHistoryQuery
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.WithWorkspace
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatDecoder
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

class HistoryContentController(
    override val workspace: Workspace
) : WsPaneController<HistoryBrowserWsItem>(), WithWorkspace {

    enum class Mode {
        TABLE, CHART
    }

    class ContentItem(
        val point: AvItem<*>,
        var records: List<AioDoubleHistoryRecord> = emptyList()
    )

    var mode = adaptiveStoreFor(Mode.CHART)

    val historyService = getService<AioHistoryApi>(transport)

    val contentItemCache = mutableMapOf<AvValueId, ContentItem>()

    val contentItems = adaptiveStoreFor(emptyList<ContentItem>())

    val chartItems: List<ChartItem<Instant, Double, AvItem<*>>>
        get() = chartItemsOrNull ?: emptyList()

    var chartItemsOrNull: List<ChartItem<Instant, Double, AvItem<*>>>? = null

    override fun accepts(pane: WsPaneType<HistoryBrowserWsItem>, modifiers: Set<EventModifier>, item: WsItem): Boolean {
        return (item is HistoryBrowserWsItem)
    }

    override fun load(pane: WsPaneType<HistoryBrowserWsItem>, modifiers: Set<EventModifier>, item: WsItem): WsPaneType<HistoryBrowserWsItem> {
        item as HistoryBrowserWsItem

        loadHistories(item)

        return pane.copy(
            name = item.name,
            data = item,
            icon = Graphics.monitoring
        )
    }

    fun loadHistories(browserItem: HistoryBrowserWsItem) {
        for (item in browserItem.items) {
            contentItemCache.clear()
            contentItemCache[item.uuid] = ContentItem(item)
            scope.launch {
                contentItemCache[item.uuid]?.records = query(item.asAvItem())
                contentItems.value = contentItemCache.values.toList()
            }
        }
    }

    suspend fun query(item: AvItem<AioPointSpec>): List<AioDoubleHistoryRecord> {
        val query = AioHistoryQuery(item.uuid.cast(), Instant.DISTANT_PAST, Instant.DISTANT_FUTURE)
        val out = mutableListOf<AioDoubleHistoryRecord>()
        historyService.query(query).decodeChunks(out)
        return out
    }

    fun List<ByteArray>.decodeChunks(out: MutableList<AioDoubleHistoryRecord>) {
        forEach {
            it.decodeChunk(out)
        }
    }

    fun ByteArray.decodeChunk(out: MutableList<AioDoubleHistoryRecord>) {
        val decoder = ProtoWireFormatDecoder(this)

        for (record in decoder.records) {
            out += record.decoder().rawInstance(record, AioDoubleHistoryRecord)
        }
    }

    fun normalize(contentItems: List<HistoryContentController.ContentItem>): List<AbstractChartDataPoint<Instant, Double>> {
        TODO()
    }
}
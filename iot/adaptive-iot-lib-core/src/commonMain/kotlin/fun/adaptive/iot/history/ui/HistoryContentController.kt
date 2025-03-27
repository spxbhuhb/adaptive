package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.chart.ws.model.WsChartContext
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.value.adaptiveStoreFor
import `fun`.adaptive.general.replaceFirst
import `fun`.adaptive.graphics.canvas.api.stroke
import `fun`.adaptive.iot.generated.resources.monitoring
import `fun`.adaptive.iot.history.AioHistoryApi
import `fun`.adaptive.iot.history.model.AioDoubleHistoryRecord
import `fun`.adaptive.iot.history.model.AioHistoryQuery
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.WithWorkspace
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType
import `fun`.adaptive.utility.secureRandom
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatDecoder
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

class HistoryContentController(
    override val workspace: Workspace
) : WsPaneController<HistoryBrowserWsItem>(), WithWorkspace {

    enum class Mode {
        TABLE, CHART
    }

    var mode = adaptiveStoreFor(Mode.CHART)

    val historyService = getService<AioHistoryApi>(transport)

    val chartItems = adaptiveStoreFor(emptyList<HistoryChartItem>())

    val instructionSets = mutableListOf<AdaptiveInstructionGroup>(
        instructionsOf(stroke(0x1f77b4)), // blue
        instructionsOf(stroke(0xff7f0e)), // orange
        instructionsOf(stroke(0x2ca02c)), // green
        instructionsOf(stroke(0xd62728)), // red
        instructionsOf(stroke(0x9467bd)), // purple
        instructionsOf(stroke(0x8c564b)), // brown
        instructionsOf(stroke(0xe377c2)), // pink
        instructionsOf(stroke(0x7f7f7f)), // gray
        instructionsOf(stroke(0xbcbd22)), // olive
        instructionsOf(stroke(0x17becf)), // cyan
        instructionsOf(stroke(0xaec7e8)), // light blue
        instructionsOf(stroke(0xffbb78)), // light orange
        instructionsOf(stroke(0x98df8a)), // light green
        instructionsOf(stroke(0xff9896)), // light red
        instructionsOf(stroke(0xc5b0d5)), // light purple
        instructionsOf(stroke(0xc49c94))  // light brown
    )

    override fun accepts(pane: WsPaneType<HistoryBrowserWsItem>, modifiers: Set<EventModifier>, item: NamedItem): Boolean {
        return (item is HistoryBrowserWsItem)
    }

    override fun load(pane: WsPaneType<HistoryBrowserWsItem>, modifiers: Set<EventModifier>, item: NamedItem): WsPaneType<HistoryBrowserWsItem> {
        item as HistoryBrowserWsItem

        loadHistories(item, modifiers.contains(EventModifier.CTRL) || modifiers.contains(EventModifier.META))

        return pane.copy(
            name = item.name,
            data = item,
            icon = Graphics.monitoring
        )
    }

    fun loadHistories(browserItem: HistoryBrowserWsItem, add: Boolean) {
        if (! add) {
            chartItems.value = emptyList()
        }

        val newChartItems = mutableListOf<HistoryChartItem>()
        val itemsToLoad = mutableListOf<AvItem<*>>()
        val usedInstructionSets = mutableSetOf<AdaptiveInstructionGroup>()

        for (avItem in browserItem.items) {
            val existing = chartItems.value.find { it.attachment?.uuid == avItem.uuid }
            if (existing != null) {
                newChartItems += existing
                usedInstructionSets += existing.instructions
            } else {
                itemsToLoad += avItem
            }

        }

        val availableInstructionSets = (instructionSets - usedInstructionSets).toMutableList()

        for (item in itemsToLoad) {

            val chartItem = HistoryChartItem(
                WsChartContext.BASIC_LINE_SERIES,
                emptyList(),
                availableInstructionSets.removeFirstOrNull() ?: generateNewInstructionSet(),
                attachment = item,
                loading = true
            ).also {
                newChartItems += it
            }

            scope.launch {
                val records = query(item)
                val copy = chartItem.copy(sourceData = records, loading = false)
                chartItems.replaceFirst(copy) { it.attachment?.uuid == item.uuid }
            }
        }

        chartItems.value = newChartItems
    }

    suspend fun query(item: AvItem<*>): List<AioDoubleHistoryRecord> {
        val query = AioHistoryQuery(item.uuid.cast(), Instant.DISTANT_PAST, Instant.DISTANT_FUTURE)
        val out = mutableListOf<AioDoubleHistoryRecord>()
        try {
            historyService.query(query).decodeChunks(out)
        } catch (e: Exception) {
            getLogger("aio:history").error("Failed to query history for ${item.uuid}", e)
        }
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

    fun generateNewInstructionSet(): AdaptiveInstructionGroup {
        return instructionsOf(stroke(secureRandom(1).first())).also { instructionSets += it }
    }
}
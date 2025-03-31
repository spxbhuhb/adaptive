package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.chart.calculation.CalculationContext
import `fun`.adaptive.chart.model.ChartAxis
import `fun`.adaptive.chart.model.ChartItem
import `fun`.adaptive.chart.model.ChartRenderContext
import `fun`.adaptive.chart.ui.temporal.doubleVerticalAxisMarkers
import `fun`.adaptive.chart.ui.temporal.temporalHorizontalAxisMarkers
import `fun`.adaptive.chart.ws.model.WsChartContext
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.general.ObservableListener
import `fun`.adaptive.graphics.canvas.api.stroke
import `fun`.adaptive.iot.common.AioTheme
import `fun`.adaptive.iot.generated.resources.chart
import `fun`.adaptive.iot.generated.resources.monitoring
import `fun`.adaptive.iot.generated.resources.table
import `fun`.adaptive.iot.history.AioHistoryApi
import `fun`.adaptive.iot.history.model.AioDoubleHistoryRecord
import `fun`.adaptive.iot.history.model.AioHistoryQuery
import `fun`.adaptive.iot.history.ui.chart.DoubleHistoryValueNormalizer
import `fun`.adaptive.iot.history.ui.model.HistoryContentConfig
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.filter.QuickFilterModel
import `fun`.adaptive.ui.fragment.layout.SplitPaneConfiguration
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.workspace.WithWorkspace
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType
import `fun`.adaptive.utility.replaceFirst
import `fun`.adaptive.utility.secureRandom
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.wireformat.protobuf.ProtoWireFormatDecoder
import kotlinx.datetime.*
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.minutes

class HistoryContentController(
    override val workspace: Workspace
) : WsPaneController<HistoryBrowserWsItem>(), WithWorkspace, ObservableListener<HistoryContentConfig> {

    enum class Mode(val labelFun: () -> String, val icon : GraphicsResourceSet) {
        TABLE({ Strings.table }, Graphics.table),
        CHART({ Strings.chart }, Graphics.monitoring);

        val label
            get() = labelFun()
    }

    var mode = storeFor<QuickFilterModel<Mode>> {
        QuickFilterModel(
            Mode.CHART,
            Mode.entries,
            { it.label }
        )
    }

    val splitConfig = storeFor { SplitPaneConfiguration(SplitVisibility.Both, SplitMethod.FixFirst, 300.0, Orientation.Horizontal) }

    val config = storeFor { HistoryContentConfig() }.also { it.addListener(this) }

    val lastConfigStart = config.value.start
    val lastConfigEnd = config.value.end
    var lastBrowserItem : HistoryBrowserWsItem? = null

    override fun onChange(value: HistoryContentConfig) {
        if (value.start != lastConfigStart || value.end != lastConfigEnd) {
            lastBrowserItem?.let { loadHistories(it, false) }
        }
    }

    val theme = AioTheme.DEFAULT

    fun switchMode(newMode : Mode) {
        mode.value = mode.value.copy(selected = newMode)
    }

    val historyService = getService<AioHistoryApi>(transport)

    val chartItems = storeFor { emptyList<HistoryChartItem>() }

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

    val xAxis = ChartAxis<Instant, AioDoubleHistoryRecord, AvItem<*>>(
        size = 49.0,
        offset = 50.0,
        axisLine = true,
        WsChartContext.BASIC_HORIZONTAL_AXIS,
        ::temporalHorizontalAxisMarkers
    )

    val yAxis = ChartAxis<Instant, AioDoubleHistoryRecord, AvItem<*>>(
        size = 49.0,
        offset = 49.0,
        axisLine = true,
        WsChartContext.BASIC_VERTICAL_AXIS,
        { c, a, s -> doubleVerticalAxisMarkers(c, a, s) { it.value } }
    )

    var chartContext = storeFor { chartRenderContext(emptyList<HistoryChartItem>()) }

    private fun chartRenderContext(items: List<HistoryChartItem>) =
        ChartRenderContext<Instant, AioDoubleHistoryRecord, AvItem<*>>(
            items,
            listOf(xAxis, yAxis),
            50.0,
            50.0,
            AioDoubleHistoryRecord.ZERO,
            { DoubleHistoryValueNormalizer(it) }
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
        lastBrowserItem = browserItem

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

            io {
                val records = query(item)
                ui {
                    val itemCopy = chartItem.copy(sourceData = records, loading = false)

                    val context = chartContext.value
                    val contextCopy = context.copy(
                        items = context.items.toMutableList().replaceFirst(itemCopy) { it.attachment?.uuid == item.uuid }
                    )

                    chartContext.value = contextCopy
                }
            }
        }

        chartContext.value = chartRenderContext(newChartItems)
    }

    suspend fun query(item: AvItem<*>): List<AioDoubleHistoryRecord> {
        val start = config.value.start.atStartOfDayIn(TimeZone.currentSystemDefault())

        val end = config.value.end
            .plus(1, DateTimeUnit.DAY)
            .atStartOfDayIn(TimeZone.currentSystemDefault())
            .minus(1, DateTimeUnit.MILLISECOND)

        val query = AioHistoryQuery(item.uuid.cast(), start, end)

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

    fun multiTableColumns(
        context: ChartRenderContext<Instant, AioDoubleHistoryRecord, AvItem<*>>
    ): Pair<List<Instant?>, List<ChartItem<Instant, AioDoubleHistoryRecord, AvItem<*>>>> {

        val iStart = context.range?.xStart ?: now()
        val iEnd = context.range?.xEnd ?: now()

        val cc = CalculationContext<Instant, AioDoubleHistoryRecord, AvItem<*>>(
            iStart,
            iEnd,
            context.normalizer.normalizedInterval(now(), now() + 15.minutes),
            context.normalizer
        ) { chartItem, start, end ->
            chartItem.sourceData[start].y
        }

        val markerColumn = cc.markers
        val valueColumns = context.items.map { it.normalize(cc.normalizer).prepareCells(cc) }

        return markerColumn to valueColumns
    }

}
package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.chart.model.ChartItem
import `fun`.adaptive.graphics.canvas.instruction.Stroke
import `fun`.adaptive.iot.generated.resources.noname
import `fun`.adaptive.iot.haystack.PhScienceMarkers
import `fun`.adaptive.iot.history.model.AioDoubleHistoryRecord
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.value.AvUiTree
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.value.AvValue
import kotlinx.datetime.Instant

fun historyColor(
    item: ChartItem<Instant, AioDoubleHistoryRecord, AvValue<*>>
) =
    item.instructions.firstInstanceOfOrNull<Stroke>()?.color ?: colors.onSurface


fun historyName(
    pane : WsPane<HistoryBrowserWsItem, HistoryContentController>,
    item: ChartItem<Instant, AioDoubleHistoryRecord, AvValue<*>>
) : String =
    historyName(
        pane.data.controller,
        item
    )

fun historyName(
    toolController : HistoryToolController,
    valueColumn: ChartItem<Instant, AioDoubleHistoryRecord, AvValue<*>>
): String {
    val item = valueColumn.attachment ?: return Strings.noname
    val itemName = item.name
    val names = historyPathNames(toolController, valueColumn.attachment!!)

    val markers = item.markers

    return when {
        names.isEmpty() -> itemName
        PhScienceMarkers.TEMP in markers -> names.last() + " °C"
        PhScienceMarkers.HUMIDITY in markers -> names.last() + " RH %"
        else -> (listOf(itemName) + names.reversed()).joinToString(" / ")
    }
}

fun historyPathNames(
    toolController : HistoryToolController,
    item: AvValue<*>
): List<String> {

    val parentId = item.parentId ?: return emptyList()

    var space: AvValue<*>? = toolController.spaceTreeStore[parentId]
    var device: AvValue<*>? = toolController.deviceTreeStore[parentId]

    when {
        space != null -> collect(space, toolController.spaceTreeStore)
        device != null -> collect(device, toolController.deviceTreeStore)
        else -> emptyList()
    }.also {
        return it
    }
}

private fun collect(start: AvValue<*>?, tree: AvUiTree<*>): List<String> {
    val names = mutableListOf<String>()
    var item = start

    while (item != null) {
        names.add(item.name)
        item = item.parentId?.let { tree[it] }
    }

    return names.reversed()

}
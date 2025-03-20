package `fun`.adaptive.chart.ws.logic

import `fun`.adaptive.chart.ws.model.WsChartPaneData
import `fun`.adaptive.chart.ws.model.WsItemChartSeries
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsPane

class WsChartContentController : WsPaneController<WsChartPaneData>() {

    override fun accepts(
        pane: WsPaneType<WsChartPaneData>,
        modifiers: Set<EventModifier>, item: WsItem
    ): Boolean {
        return (item is WsItemChartSeries)
    }

    override fun load(
        pane: WsPaneType<WsChartPaneData>,
        modifiers: Set<EventModifier>, item: WsItem
    ): WsPaneType<WsChartPaneData> {

        check(item is WsItemChartSeries)
        pane.data.items.value += item
        return pane
    }

}
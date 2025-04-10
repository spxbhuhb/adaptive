package `fun`.adaptive.chart.ws.logic

import `fun`.adaptive.chart.ws.model.WsChartPaneData
import `fun`.adaptive.chart.ws.model.WsItemChartSeries
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.logic.WsPaneController
import `fun`.adaptive.ui.workspace.logic.WsPaneType
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.ui.workspace.Workspace

class WsChartContentController(
    override val workspace: Workspace
) : WsPaneController<WsChartPaneData>() {

    override fun accepts(
        pane: WsPaneType<WsChartPaneData>,
        modifiers: Set<EventModifier>, item: NamedItem
    ): Boolean {
        return (item is WsItemChartSeries)
    }

    override fun load(
        pane: WsPaneType<WsChartPaneData>,
        modifiers: Set<EventModifier>, item: NamedItem
    ): WsPaneType<WsChartPaneData> {

        check(item is WsItemChartSeries)
        pane.data.items.value += item
        return pane
    }

}
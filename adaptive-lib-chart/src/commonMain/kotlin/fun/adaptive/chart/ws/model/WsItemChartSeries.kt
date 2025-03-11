package `fun`.adaptive.chart.ws.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.chart.model.ChartSeries
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsItemType
import `fun`.adaptive.utility.UUID

/**
 * A workspace item that represents one series of whatever data
 * that can be shown on a chart.
 */
@Adat
class WsItemChartSeries(
    override val icon: GraphicsResourceSet,
    override val name: String,
    override val type: WsItemType,
    override val tooltip: String?,
    val seriesUuid: UUID<ChartSeries>,
    val children : List<WsItemChartSeries> = emptyList()
) : WsItem() {

    fun toTreeItem(context : WsChartContext): TreeItem =
        TreeItem(
            icon,
            name,
            children = children.map { it.toTreeItem(context) },
            data = seriesUuid
        ) { item, modifiers -> if (children.isEmpty()) context.openChart(this) }

}
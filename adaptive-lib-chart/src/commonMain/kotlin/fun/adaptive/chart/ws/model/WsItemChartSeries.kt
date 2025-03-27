package `fun`.adaptive.chart.ws.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.model.NamedItemType
import `fun`.adaptive.utility.UUID

/**
 * A workspace item that represents one series of whatever data
 * that can be shown on a chart.
 */
@Adat
class WsItemChartSeries(
    override val name: String,
    override val type: NamedItemType,
    val seriesUuid: UUID<Any>,
    val children : List<WsItemChartSeries> = emptyList()
) : NamedItem() {

    fun toTreeItem(context: WsChartContext, parent: TreeItem<WsItemChartSeries>?): TreeItem<WsItemChartSeries> {

        val config = context[this]

        TreeItem(
            config.icon,
            name,
            data = this,
            parent = parent
        ).also { item ->
            item.children = children.map { it.toTreeItem(context, item) }
            return item
        }
    }

}
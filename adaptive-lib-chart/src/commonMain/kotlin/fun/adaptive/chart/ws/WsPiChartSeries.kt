package `fun`.adaptive.chart.ws

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.chart.database
import `fun`.adaptive.chart.model.ChartSeries
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.utility.UUID

@Adat
class WsPiChartSeries(
    override val icon: GraphicsResourceSet,
    override val name: String,
    val seriesUuid: UUID<ChartSeries>
) : WsProjectItem() {

    fun toTreeItem(context : WsChartContext): TreeItem =
        TreeItem(
            Graphics.database,
            name,
            emptyList(),
            onClick = { context.openChart(this) },
            data = seriesUuid
        )

}
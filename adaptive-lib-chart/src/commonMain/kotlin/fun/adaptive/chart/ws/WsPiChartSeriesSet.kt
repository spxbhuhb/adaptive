package `fun`.adaptive.chart.ws

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.builtin.folder
import `fun`.adaptive.ui.tree.TreeItem

@Adat
class WsPiChartSeriesSet(
    override val icon: GraphicsResourceSet,
    override val name : String,
    val subsets : List<WsPiChartSeriesSet>,
    val items : List<WsPiChartSeries>
) : WsProjectItem() {

    fun toTreeItem(context: WsChartContext) : TreeItem =
        TreeItem(
            Graphics.folder,
            title = name,
            children = subsets.map { it.toTreeItem(context) } + items.map { it.toTreeItem(context) },
            onClick = {  }
        )

}
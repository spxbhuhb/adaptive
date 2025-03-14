package `fun`.adaptive.grove.apm.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.apm.ApmWsContext
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsItemTooltip
import `fun`.adaptive.ui.workspace.model.WsItemType

@Adat
class ApmWsItem(
    override val name: String,
    override val type: WsItemType,
    override val tooltip: WsItemTooltip? = null,
    val path: String
) : WsItem() {

    fun toTreeItem(context: ApmWsContext): TreeItem<ApmWsItem> {
        val config = context[this]

        TreeItem<ApmWsItem>(
            config.icon,
            name,
            data = this,
            parent = null
        ).also {
            return it
        }
    }

}
package `fun`.adaptive.grove.apm.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.resources.folder
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsItemType

@Adat
class ApmWsItem(
    override val name: String,
    override val icon: GraphicsResourceSet,
    override val type: WsItemType,
    override val tooltip: String?,
    val path: String
) : WsItem() {

    fun toTreeItem(): TreeItem<ApmWsItem> =
        TreeItem<ApmWsItem>(
            Graphics.folder,
            name,
            data = this,
            parent = null
        )

}
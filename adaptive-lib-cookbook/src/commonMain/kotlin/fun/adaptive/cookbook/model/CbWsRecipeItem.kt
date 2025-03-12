package `fun`.adaptive.cookbook.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.cookbook.CbWsContext
import `fun`.adaptive.cookbook.dining
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsItemType

@Adat
class CbWsRecipeItem(
    override val name: String,
    override val icon: GraphicsResourceSet = Graphics.dining,
    override val type: WsItemType = CbWsContext.RECIPE_ITEM_TYPE,
    override val tooltip: String? = null,
    val children : List<CbWsRecipeItem> = emptyList(),
    val key: String? = null
) : WsItem() {

    fun toTreeItem(context : CbWsContext, onClick: (TreeItem<CbWsRecipeItem>, Set<EventModifier>) -> Unit): TreeItem<CbWsRecipeItem> =
        TreeItem(
            Graphics.dining,
            name,
            children = children.map { it.toTreeItem(context, onClick) },
            data = this,
            onClick = onClick
        )

}
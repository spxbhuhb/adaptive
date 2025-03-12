package `fun`.adaptive.cookbook.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.cookbook.CbWsContext
import `fun`.adaptive.cookbook.dining
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsItemType

@Adat
class CbWsRecipeItem(
    override val name: String,
    override val icon: GraphicsResourceSet = Graphics.dining,
    override val type: WsItemType = CbWsContext.RECIPE_ITEM_TYPE,
    override val tooltip: String? = null,
    val children: List<CbWsRecipeItem> = emptyList(),
    val key: String? = null
) : WsItem() {

    fun toTreeItem(parent: TreeItem<CbWsRecipeItem>?): TreeItem<CbWsRecipeItem> =
        TreeItem(
            Graphics.dining,
            name,
            data = this,
            parent = parent
        ).also { item ->
            item.children = children.map { it.toTreeItem(item) }
        }

}
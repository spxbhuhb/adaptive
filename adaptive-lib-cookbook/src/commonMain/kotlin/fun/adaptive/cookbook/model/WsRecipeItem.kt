package `fun`.adaptive.cookbook.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.cookbook.WsCookbookContext
import `fun`.adaptive.cookbook.dining
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsItemType

@Adat
class WsRecipeItem(
    override val name: String,
    override val icon: GraphicsResourceSet = Graphics.dining,
    override val type: WsItemType = WsCookbookContext.RECIPE_ITEM_TYPE,
    override val tooltip: String? = null,
    val key: String
) : WsItem() {

    fun toTreeItem(onClick: (TreeItem) -> Unit): TreeItem =
        TreeItem(
            Graphics.dining,
            name,
            emptyList(),
            onClick = onClick,
            data = this
        )

}
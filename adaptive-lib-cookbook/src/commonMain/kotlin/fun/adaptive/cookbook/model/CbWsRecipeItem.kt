package `fun`.adaptive.cookbook.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.cookbook.CbWsContext
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsItemType

@Adat
class CbWsRecipeItem(
    override val name: String,
    override val type: WsItemType = CbWsContext.WSIT_CB_RECIPE,
    val children: List<CbWsRecipeItem> = emptyList(),
    val key: String? = null
) : WsItem() {

    fun toTreeItem(context: CbWsContext, parent: TreeItem<CbWsRecipeItem>?): TreeItem<CbWsRecipeItem> {
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
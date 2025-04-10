package `fun`.adaptive.sandbox.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.sandbox.CbWsContext
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.model.NamedItemType

@Adat
class CbWsRecipeItem(
    override val name: String,
    override val type: NamedItemType = CbWsContext.WSIT_CB_RECIPE,
    val children: List<CbWsRecipeItem> = emptyList(),
    val key: String? = null
) : NamedItem() {

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
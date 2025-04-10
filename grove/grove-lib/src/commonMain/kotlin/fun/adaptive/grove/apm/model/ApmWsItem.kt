package `fun`.adaptive.grove.apm.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.apm.ApmWsContext
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.model.NamedItemType

@Adat
class ApmWsItem(
    override val name: String,
    override val type: NamedItemType,
    val path: String
) : NamedItem() {

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
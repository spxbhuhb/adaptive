package `fun`.adaptive.document.ws.browser

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.model.NamedItemType
import `fun`.adaptive.value.item.AvItem

@Adat
class DocBrowserWsItem(
    override val name: String,
    override val type: NamedItemType,
    val config: DocBrowserConfig,
    val item: AvItem<*>
) : NamedItem() {

    val uuid
        get() = item.uuid

}
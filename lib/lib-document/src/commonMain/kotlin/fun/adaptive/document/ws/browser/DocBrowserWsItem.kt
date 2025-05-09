package `fun`.adaptive.document.ws.browser

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.model.NamedItemType
import `fun`.adaptive.value.AvValue

@Adat
class DocBrowserWsItem(
    override val name: String,
    override val type: NamedItemType,
    val config: DocBrowserConfig,
    val item: AvValue<*>
) : NamedItem() {

    val uuid
        get() = item.uuid

}
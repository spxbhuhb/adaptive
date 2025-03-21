package `fun`.adaptive.document.ws.browser

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsItemType
import `fun`.adaptive.value.item.AvItem

@Adat
class DocBrowserWsItem(
    override val name: String,
    override val type: WsItemType,
    val config: DocBrowserConfig,
    val item: AvItem
) : WsItem() {

    val uuid
        get() = item.uuid

}
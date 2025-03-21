package `fun`.adaptive.iot.space.ui.browser

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsItemType

@Adat
class SpaceBrowserWsItem(
    override val name: String,
    override val type: WsItemType,
    val config: SpaceBrowserConfig,
    val item: AvItem
) : WsItem() {

    val uuid
        get() = item.uuid

}
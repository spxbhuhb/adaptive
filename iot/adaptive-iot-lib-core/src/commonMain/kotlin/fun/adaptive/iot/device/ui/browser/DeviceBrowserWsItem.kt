package `fun`.adaptive.iot.device.ui.browser

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsItemType

@Adat
class DeviceBrowserWsItem(
    override val name: String,
    override val type: WsItemType,
    val config: DeviceBrowserConfig,
    val item: AvItem<*>
) : WsItem() {

    val uuid
        get() = item.uuid

}
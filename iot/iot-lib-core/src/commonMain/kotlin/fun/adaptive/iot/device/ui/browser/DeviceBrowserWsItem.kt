package `fun`.adaptive.iot.device.ui.browser

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.model.NamedItemType

@Adat
class DeviceBrowserWsItem(
    override val name: String,
    override val type: NamedItemType,
    val config: DeviceBrowserConfig,
    val item: AvValue<*>
) : NamedItem() {

    val uuid
        get() = item.uuid

}
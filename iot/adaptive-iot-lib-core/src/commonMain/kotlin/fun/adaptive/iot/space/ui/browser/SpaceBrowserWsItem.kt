package `fun`.adaptive.iot.space.ui.browser

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.model.NamedItemType

@Adat
class SpaceBrowserWsItem(
    override val name: String,
    override val type: NamedItemType,
    val config: SpaceBrowserConfig,
    val item: AvItem<*>
) : NamedItem() {

    val uuid
        get() = item.uuid

}
package `fun`.adaptive.iot.domain.rht.ui.controller

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.value.item.AvItem

@Adat
class RhtWsItem(
    override val name: String,
    val toolController: RhtBrowserToolController,
    val avItem: AvItem<*>
) : NamedItem() {

    override val type
        get() = WSIT_RHT_ITEM

    val uuid
        get() = avItem.uuid

    val spacePath
        get() = toolController.valueTreeStore.pathNames(this.avItem)

    companion object {
        const val WSIT_RHT_ITEM = "aio:rht:item"
    }
}
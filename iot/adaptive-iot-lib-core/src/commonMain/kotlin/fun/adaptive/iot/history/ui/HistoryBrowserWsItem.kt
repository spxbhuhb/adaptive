package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.space.ui.browser.SpaceBrowserConfig
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsItemType

@Adat
class HistoryBrowserWsItem(
    override val name: String,
    override val type: WsItemType,
    val items: List<AvItem<*>>
) : WsItem()
package `fun`.adaptive.iot.space.ui

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.item.AioItemId
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsItemType

@Adat
class SpaceBrowserWsItem(
    override val name: String,
    val config: SpaceToolConfig,
    val spaceId: AioItemId,
    val data: Any? = null,
    override val type: WsItemType = AioWsContext.WSIT_MEASUREMENT_LOCATION
) : WsItem()
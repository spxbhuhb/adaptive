package `fun`.adaptive.iot.model.space

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.model.AioSpaceId
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.workspace.model.WsItem
import `fun`.adaptive.ui.workspace.model.WsItemType

@Adat
class SpaceBrowserWsItem(
    override val name: String,
    val config: SpaceBrowserConfig,
    val spaceId: AioSpaceId,
    val data: Any? = null,
    override val type: WsItemType = AioWsContext.WSIT_MEASUREMENT_LOCATION
) : WsItem()
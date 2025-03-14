package `fun`.adaptive.iot.model.device

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.model.AioDeviceId
import `fun`.adaptive.iot.model.AioNetworkId
import `fun`.adaptive.iot.model.AioProjectId
import `fun`.adaptive.iot.model.AioSpaceId
import `fun`.adaptive.iot.model.project.AioProjectItem
import `fun`.adaptive.iot.model.project.FriendlyItemId
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.workspace.model.WsItemType

@Adat
class AioDevice(
    override val uuid: AioDeviceId,
    override val projectId: AioProjectId,
    override val friendlyId: FriendlyItemId,
    override val name: String,
    override val type: WsItemType = AioWsContext.WSIT_DEVICE,
    val networkId : AioNetworkId,
    val spaceId : AioSpaceId
) : AioProjectItem<AioDevice>()
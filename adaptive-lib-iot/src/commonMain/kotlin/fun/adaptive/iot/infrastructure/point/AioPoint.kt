package `fun`.adaptive.iot.infrastructure.point

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.common.AioMarkerSet
import `fun`.adaptive.iot.infrastructure.device.AioDeviceId
import `fun`.adaptive.iot.project.AioProjectId
import `fun`.adaptive.iot.space.AioSpaceId
import `fun`.adaptive.iot.project.model.AioProjectItem
import `fun`.adaptive.iot.project.FriendlyItemId
import `fun`.adaptive.iot.infrastructure.network.AioNetworkId
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.workspace.model.WsItemType

@Adat
class AioPoint(
    override val uuid: AioPointId,
    override val projectId: AioProjectId,
    override val friendlyId: FriendlyItemId,
    override val name: String,
    override val type: WsItemType = AioWsContext.WSIT_POINT,
    override val markers : AioMarkerSet = emptySet(),
    val networkId : AioNetworkId? = null,
    val deviceId : AioDeviceId? = null,
    val spaceId : AioSpaceId? = null
) : AioProjectItem<AioPoint>()
package `fun`.adaptive.iot.device

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.common.AioMarkerSet
import `fun`.adaptive.iot.project.AioProjectId
import `fun`.adaptive.iot.space.AioSpaceId
import `fun`.adaptive.iot.curval.AioValueId
import `fun`.adaptive.iot.project.model.AioProjectItem
import `fun`.adaptive.iot.project.FriendlyItemId
import `fun`.adaptive.iot.network.AioNetworkId
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.workspace.model.WsItemType

@Adat
class AioDevice(
    override val uuid: AioDeviceId,
    override val projectId: AioProjectId,
    override val friendlyId: FriendlyItemId,
    override val name: String,
    override val type: WsItemType = AioWsContext.Companion.WSIT_DEVICE,
    override val markers : AioMarkerSet = emptySet(),
    val networkId : AioNetworkId? = null,
    val spaceId : AioSpaceId? = null,
    val valueId1 : AioValueId? = null,
    val valueId2 : AioValueId? = null,
    val valueId3 : AioValueId? = null
) : AioProjectItem<AioDevice>()
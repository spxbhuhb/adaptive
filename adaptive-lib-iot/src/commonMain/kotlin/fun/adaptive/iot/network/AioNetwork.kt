package `fun`.adaptive.iot.network

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.common.AioMarkerSet
import `fun`.adaptive.iot.infrastructure.model.AioInfrastructureItem
import `fun`.adaptive.iot.infrastructure.model.AioInfrastructureItemType
import `fun`.adaptive.iot.project.AioProjectId
import `fun`.adaptive.iot.project.model.AioProjectItem
import `fun`.adaptive.iot.project.FriendlyItemId
import `fun`.adaptive.iot.space.AioSpaceId
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.workspace.model.WsItemType

@Adat
class AioNetwork(
    override val uuid: AioNetworkId,
    override val projectId: AioProjectId,
    override val friendlyId: FriendlyItemId,
    override val name: String,
    override val type: WsItemType = AioWsContext.Companion.WSIT_NETWORK,
    override val markers : AioMarkerSet = emptySet(),
    val spaceId : AioSpaceId? = null
) : AioProjectItem<AioNetwork>() {

    fun toInfrastructureItem() = AioInfrastructureItem(
        uuid = uuid.cast(),
        projectId = projectId,
        friendlyId = friendlyId,
        name = name,
        markers = markers,
        spaceId = spaceId,
        itemType = AioInfrastructureItemType.Network
    )

}
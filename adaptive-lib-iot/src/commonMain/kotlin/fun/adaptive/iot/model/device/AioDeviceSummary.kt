package `fun`.adaptive.iot.model.device

import `fun`.adaptive.iot.model.AioDeviceId
import `fun`.adaptive.iot.model.AioNetworkId
import `fun`.adaptive.iot.model.AioProjectId
import `fun`.adaptive.iot.model.AioSpaceId
import `fun`.adaptive.iot.model.device.point.AioValueSummary
import `fun`.adaptive.iot.model.project.FriendlyItemId
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.workspace.model.WsItemType

class AioDeviceSummary(
    val uuid: AioDeviceId,
    val projectId: AioProjectId,
    val friendlyId: FriendlyItemId,
    val name: String,
    val type: WsItemType = AioWsContext.WSIT_DEVICE,
    val networkId: AioNetworkId,
    val spaceId: AioSpaceId,
    val value1 : AioValueSummary?,
    val value2 : AioValueSummary?,
    val value3 : AioValueSummary?,
    val actions : AioDeviceAction
)
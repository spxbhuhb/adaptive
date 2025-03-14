package `fun`.adaptive.iot.model.measurement

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.model.AioDeviceId
import `fun`.adaptive.iot.model.AioMeasurementLocationId
import `fun`.adaptive.iot.model.AioProjectId
import `fun`.adaptive.iot.model.AioSpaceId
import `fun`.adaptive.iot.model.project.AioProjectItem
import `fun`.adaptive.iot.model.project.FriendlyItemId
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.workspace.model.WsItemType

@Adat
class AioMeasurementLocation(
    override val uuid: AioMeasurementLocationId,
    override val projectId: AioProjectId,
    override val friendlyId: FriendlyItemId,
    override val name: String,
    override val type: WsItemType = AioWsContext.WSIT_MEASUREMENT_LOCATION,
    val deviceId: AioDeviceId,
    val spaceId : AioSpaceId
) : AioProjectItem<AioMeasurementLocation>()
package `fun`.adaptive.iot.model.measurement.point

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.model.AioMeasurementLocationId
import `fun`.adaptive.iot.model.AioMeasurementPointId
import `fun`.adaptive.iot.model.AioProjectId
import `fun`.adaptive.iot.model.AioSpaceId
import `fun`.adaptive.iot.model.project.AioProjectItem
import `fun`.adaptive.iot.model.project.FriendlyItemId
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.workspace.model.WsItemType

@Adat
class AioMeasurementPoint(
    override val uuid: AioMeasurementPointId,
    override val projectId: AioProjectId,
    override val friendlyId: FriendlyItemId,
    override val name: String,
    override val type: WsItemType = AioWsContext.WSIT_MEASUREMENT_POINT,
    val locationId: AioMeasurementLocationId,
    val spaceId : AioSpaceId
) : AioProjectItem<AioMeasurementPoint>()
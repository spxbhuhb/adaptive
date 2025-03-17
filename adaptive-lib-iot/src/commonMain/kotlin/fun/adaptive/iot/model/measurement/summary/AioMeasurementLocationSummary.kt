package `fun`.adaptive.iot.model.measurement.summary

import `fun`.adaptive.iot.model.*
import `fun`.adaptive.iot.model.project.FriendlyItemId
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.workspace.model.WsItemType

class AioMeasurementLocationSummary(
    val uuid: AioMeasurementLocationId,
    val projectId: AioProjectId,
    val friendlyId: FriendlyItemId,
    val name: String,
    val type: WsItemType = AioWsContext.Companion.WSIT_MEASUREMENT_LOCATION,
    val measurementType : AioMeasurementLocationType,
    val device: AioDeviceId,
    val spaceId: AioSpaceId,
    val value1 : AioValueSummary?,
    val value2 : AioValueSummary?,
    val value3 : AioValueSummary?
)
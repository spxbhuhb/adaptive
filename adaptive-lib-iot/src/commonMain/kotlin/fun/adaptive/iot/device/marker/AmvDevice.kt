package `fun`.adaptive.iot.device.marker

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.item.AvMarkerValue
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.utility.UUID.Companion.uuid7
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

@Adat
data class AmvDevice(
    override val uuid: AvValueId,
    override val timestamp: Instant,
    override val status: AvStatus,
    override val owner: AvValueId,
    val notes : String? = null
) : AvMarkerValue() {

    constructor(
        owner: AvValueId
    ) : this(
        uuid = uuid7(),
        timestamp = now(),
        status = AvStatus.OK,
        owner = owner
    )
    override val markerName: String
        get() = DeviceMarkers.DEVICE

}
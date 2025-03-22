package `fun`.adaptive.iot.space.marker

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.item.AvMarkerValue
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.utility.UUID.Companion.uuid7
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

@Adat
data class AmvSpace(
    override val uuid: AvValueId,
    override val timestamp: Instant,
    override val status: AvStatus,
    override val owner: AvValueId,
    val area: Double,
    val notes : String? = null
) : AvMarkerValue() {

    constructor(
        owner: AvValueId,
        area: Double
    ) : this(
        uuid = uuid7(),
        timestamp = now(),
        status = AvStatus.OK,
        owner = owner,
        area = area
    )
    override val markerName: String
        get() = SpaceMarkers.SPACE

}
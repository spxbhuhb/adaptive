package `fun`.adaptive.iot.space.markers

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.item.AioMarkerValue
import `fun`.adaptive.iot.item.AioStatus
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.utility.UUID.Companion.uuid7
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

@Adat
data class AmvSpace(
    override val uuid: AioValueId,
    override val timestamp: Instant,
    override val status: AioStatus,
    override val owner: AioValueId,
    val area: Double,
    val notes : String? = null
) : AioMarkerValue() {

    constructor(
        owner: AioValueId,
        area: Double
    ) : this(
        uuid = uuid7(),
        timestamp = now(),
        status = AioStatus.OK,
        owner = owner,
        area = area
    )
    override val markerName: String
        get() = SpaceMarkers.SPACE

}
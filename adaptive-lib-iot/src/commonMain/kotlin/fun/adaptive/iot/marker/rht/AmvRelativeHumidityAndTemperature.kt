package `fun`.adaptive.iot.marker.rht

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.item.AioMarkerValue
import `fun`.adaptive.iot.item.AioStatus
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.utility.UUID.Companion.uuid7
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

@Adat
class AmvRelativeHumidityAndTemperature(
    override val uuid: AioValueId,
    override val timestamp: Instant,
    override val status: AioStatus,
    override val owner: AioValueId,
    override val markerName: String,
    val relativeHumidity: Double,
    val temperature: Double
) : AioMarkerValue() {

    constructor(owner: AioValueId, relativeHumidity: Double, temperature: Double) : this(
        uuid = uuid7(),
        timestamp = now(),
        status = AioStatus.OK,
        owner = owner,
        markerName = MARKER_NAME,
        relativeHumidity = relativeHumidity,
        temperature = temperature
    )

    companion object {
        const val MARKER_NAME = "relative-humidity-and-temperature"
        const val WSIT_RHT_BROWSER_ITEM = "aio:rht:browser:item"
        const val RHT_LIST_ITEM = "rht:list:item"
    }
}
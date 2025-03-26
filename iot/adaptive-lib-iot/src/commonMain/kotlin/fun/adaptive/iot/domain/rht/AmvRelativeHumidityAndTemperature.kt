package `fun`.adaptive.iot.domain.rht

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.item.AvMarkerValue
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.utility.UUID.Companion.uuid7
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

@Adat
class AmvRelativeHumidityAndTemperature(
    override val uuid: AvValueId,
    override val timestamp: Instant,
    override val status: AvStatus,
    override val parentId: AvValueId,
    override val markerName: String,
    val relativeHumidity: Double,
    val temperature: Double
) : AvMarkerValue() {

    constructor(parentId: AvValueId, relativeHumidity: Double, temperature: Double) : this(
        uuid = uuid7(),
        timestamp = now(),
        status = AvStatus.OK,
        parentId = parentId,
        markerName = MARKER_NAME,
        relativeHumidity = relativeHumidity,
        temperature = temperature
    )

    companion object {
        const val MARKER_NAME = "relative-humidity-and-temperature"
        const val WSIT_RHT_BROWSER_ITEM = "aio:rht:browser:item"
        const val RHT_LIST_HEADER = "rht:list:header"
        const val RHT_LIST_ITEM = "rht:list:item"
    }
}
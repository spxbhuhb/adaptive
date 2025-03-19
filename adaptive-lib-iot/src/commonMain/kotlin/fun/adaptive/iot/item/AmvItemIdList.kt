package `fun`.adaptive.iot.item

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.utility.UUID.Companion.uuid7
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

@Adat
data class AmvItemIdList(
    override val uuid: AioValueId,
    override val timestamp: Instant,
    override val status: AioStatus,
    override val owner: AioValueId,
    override val markerName: String,
    val itemIds: List<AioValueId>
) : AioMarkerValue() {

    constructor(
        owner: AioValueId,
        markerName: String,
        itemIds: List<AioValueId> = emptyList()
    ) : this(
        uuid = uuid7(),
        timestamp = now(),
        status = AioStatus.OK,
        owner = owner,
        markerName = markerName,
        itemIds = itemIds
    )

}
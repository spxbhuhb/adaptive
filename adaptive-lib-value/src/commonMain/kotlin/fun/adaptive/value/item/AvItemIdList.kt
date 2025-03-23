package `fun`.adaptive.value.item

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.utility.UUID.Companion.uuid7
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

@Adat
data class AvItemIdList(
    override val uuid: AvValueId,
    override val timestamp: Instant,
    override val status: AvStatus,
    override val parentId: AvValueId,
    override val markerName: String,
    val itemIds: List<AvValueId>
) : AvMarkerValue() {

    constructor(
        parentId: AvValueId,
        markerName: String,
        itemIds: List<AvValueId> = emptyList()
    ) : this(
        uuid = uuid7(),
        timestamp = now(),
        status = AvStatus.OK,
        parentId = parentId,
        markerName = markerName,
        itemIds = itemIds
    )

}
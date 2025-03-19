package `fun`.adaptive.iot.space

import `fun`.adaptive.iot.item.AmvItemIdList
import `fun`.adaptive.iot.space.markers.SpaceMarkers
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.iot.value.AioValueWorker
import `fun`.adaptive.iot.value.operation.AvoAdd
import `fun`.adaptive.iot.value.operation.AvoUpdate
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.utility.waitForReal
import kotlin.time.Duration.Companion.seconds

suspend fun addSpaceTop(worker: AioValueWorker, spaceId: AioValueId) {
    val spaceTops = worker.queryByMarker(SpaceMarkers.TOP_SPACES)

    val original = spaceTops.firstOrNull()
    val new: AmvItemIdList

    if (original != null) {
        check(original is AmvItemIdList) { "Expected AmvItemIdList, got $spaceTops" }
        new = original.copy(itemIds = original.itemIds + spaceId)
    } else {
        new = AmvItemIdList(owner = uuid7(), markerName = SpaceMarkers.TOP_SPACES, listOf(spaceId))
    }

    worker.addOrUpdate(new)

    waitForReal(1.seconds) { worker.isIdle }
}

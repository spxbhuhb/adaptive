package `fun`.adaptive.iot.space

import `fun`.adaptive.value.item.AvRefList
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.utility.waitForReal
import kotlin.time.Duration.Companion.seconds

suspend fun addSpaceTop(worker: AvValueWorker, spaceId: AvValueId) {
    val spaceTops = worker.queryByMarker(SpaceMarkers.TOP_SPACES)

    val original = spaceTops.firstOrNull()
    val new: AvRefList

    if (original != null) {
        check(original is AvRefList) { "Expected AmvItemIdList, got $spaceTops" }
        new = original.copy(refs = original.refs + spaceId)
    } else {
        new = AvRefList(parentId = uuid7(), markerName = SpaceMarkers.TOP_SPACES, listOf(spaceId))
    }

    worker.queueAddOrUpdate(new)

    waitForReal(1.seconds) { worker.isIdle }
}

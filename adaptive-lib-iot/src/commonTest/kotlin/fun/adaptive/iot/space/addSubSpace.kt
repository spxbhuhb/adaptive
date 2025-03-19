package `fun`.adaptive.iot.space

import `fun`.adaptive.iot.item.AmvItemIdList
import `fun`.adaptive.iot.space.markers.SpaceMarkers
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.iot.value.AioValueWorker
import `fun`.adaptive.iot.value.operation.AvoAdd
import `fun`.adaptive.iot.value.operation.AvoUpdate
import `fun`.adaptive.utility.waitForReal
import kotlin.time.Duration.Companion.seconds

suspend fun addSubSpace(worker: AioValueWorker, spaceId: AioValueId, subSpaceId: AioValueId) {
    val space = worker.item(spaceId)

    val originalId = space[SpaceMarkers.SUB_SPACES]

    if (originalId == null) {

        val subSpacesSpec = AmvItemIdList(owner = space.uuid, markerName = SpaceMarkers.SUB_SPACES, listOf(subSpaceId))

        worker.transaction(
            listOf(
                AvoUpdate(space.copyWith(subSpacesSpec)),
                AvoAdd(subSpacesSpec)
            )
        )
    } else {

        val original = worker[originalId]
        check(original is AmvItemIdList) { "Expected AmvItemIdList, got $original" }

        worker.update(original.copy(itemIds = original.itemIds + subSpaceId))
    }

    waitForReal(1.seconds) { worker.isIdle }
}

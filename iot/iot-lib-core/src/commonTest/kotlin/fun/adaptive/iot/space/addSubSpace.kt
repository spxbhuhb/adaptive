package `fun`.adaptive.iot.space

import `fun`.adaptive.value.item.AvRefList
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.operation.AvoAdd
import `fun`.adaptive.value.operation.AvoUpdate
import `fun`.adaptive.utility.waitForReal
import kotlin.time.Duration.Companion.seconds

suspend fun addSubSpace(worker: AvValueWorker, spaceId: AvValueId, subSpaceId: AvValueId) {
    val space = worker.item(spaceId)

    val originalId = space[SpaceMarkers.SUB_SPACES]

    if (originalId == null) {

        val subSpacesSpec = AvRefList(parentId = space.uuid, markerName = SpaceMarkers.SUB_SPACES, listOf(subSpaceId))

        worker.queueTransaction(
            listOf(
                AvoUpdate(space.copyWith(subSpacesSpec)),
                AvoAdd(subSpacesSpec)
            )
        )
    } else {

        val original = worker[originalId]
        check(original is AvRefList) { "Expected AmvItemIdList, got $original" }

        worker.queueUpdate(original.copy(refs = original.refs + subSpaceId))
    }

    waitForReal(1.seconds) { worker.isIdle }
}

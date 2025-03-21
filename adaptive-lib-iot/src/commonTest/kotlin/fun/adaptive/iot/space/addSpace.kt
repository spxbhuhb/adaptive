package `fun`.adaptive.iot.space

import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.item.FriendlyItemId
import `fun`.adaptive.iot.space.markers.AmvSpace
import `fun`.adaptive.iot.space.markers.SpaceMarkers
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.utility.waitForReal
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.seconds

suspend fun addSpace(worker: AvValueWorker, name: String, friendlyItemId: FriendlyItemId, spaceType: AvMarker): AvValueId {
    val spaceUuid = uuid7<AvValue>()

    val spaceSpec = AmvSpace(owner = spaceUuid, area = 12.3)

    val space = AvItem(
        name,
        AioWsContext.WSIT_SPACE,
        spaceUuid,
        now(),
        AvStatus.OK,
        friendlyItemId,
        markersOrNull = mutableMapOf(
            SpaceMarkers.SPACE to spaceSpec.uuid,
            spaceType to null
        ),
        parentId = null
    )

    worker.queueAddAll(space, spaceSpec)

    waitForReal(1.seconds) { worker.isIdle }

    return spaceUuid
}
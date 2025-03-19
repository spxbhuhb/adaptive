package `fun`.adaptive.iot.space

import `fun`.adaptive.iot.item.AioItem
import `fun`.adaptive.iot.item.AioMarker
import `fun`.adaptive.iot.item.AioStatus
import `fun`.adaptive.iot.item.FriendlyItemId
import `fun`.adaptive.iot.space.markers.AmvSpace
import `fun`.adaptive.iot.space.markers.SpaceMarkers
import `fun`.adaptive.iot.value.AioValue
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.iot.value.AioValueWorker
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.utility.waitForReal
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.seconds

suspend fun addSpace(worker: AioValueWorker, name: String, friendlyItemId: FriendlyItemId, spaceType: AioMarker): AioValueId {
    val spaceUuid = uuid7<AioValue>()

    val spaceSpec = AmvSpace(owner = spaceUuid, area = 12.3)

    val space = AioItem(
        name,
        AioWsContext.WSIT_SPACE,
        spaceUuid,
        now(),
        AioStatus.OK,
        friendlyItemId,
        markersOrNull = mutableMapOf(
            SpaceMarkers.SPACE to spaceSpec.uuid,
            spaceType to null
        ),
        parentId = null
    )

    worker.addAll(space, spaceSpec)

    waitForReal(1.seconds) { worker.isIdle }

    return spaceUuid
}
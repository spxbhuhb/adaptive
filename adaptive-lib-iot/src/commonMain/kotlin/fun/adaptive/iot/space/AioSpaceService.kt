package `fun`.adaptive.iot.space

import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.item.AioItem
import `fun`.adaptive.iot.item.AioMarker
import `fun`.adaptive.iot.item.AioStatus
import `fun`.adaptive.iot.item.AmvItemIdList
import `fun`.adaptive.iot.space.markers.AmvSpace
import `fun`.adaptive.iot.space.markers.SpaceMarkers
import `fun`.adaptive.iot.value.AioValue
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.iot.value.AioValueWorker
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.uuid7
import kotlinx.datetime.Clock.System.now

class AioSpaceService : AioSpaceApi, ServiceImpl<AioSpaceService> {

    companion object {
        lateinit var worker: AioValueWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isServer)
        worker = safeAdapter.firstImpl<AioValueWorker>()
    }

    override suspend fun addSpace(name: String, spaceType: AioMarker, parentId: AioValueId?): AioValueId {
        publicAccess()

        val spaceId = uuid7<AioValue>()

        worker.execute { context ->

            val spaceSpec = AmvSpace(owner = spaceId, area = 0.0)

            val space = AioItem(
                name,
                AioWsContext.WSIT_SPACE,
                spaceId,
                now(),
                AioStatus.OK,
                context.nextFriendlyId(SpaceMarkers.SPACE),
                markersOrNull = mutableMapOf(
                    SpaceMarkers.SPACE to spaceSpec.uuid,
                    spaceType to null
                ),
                parentId = parentId
            )

            context += space
            context += spaceSpec

            if (parentId == null) {
                addTop(context, spaceId)
            }

        }

        return spaceId
    }

    private fun addTop(context: AioValueWorker.WorkerComputeContext, spaceId: UUID<AioValue>) {
        val topSpaces = context.queryByMarker(SpaceMarkers.TOP_SPACES)

        val original = topSpaces.firstOrNull()
        val new: AmvItemIdList

        if (original != null) {
            check(original is AmvItemIdList) { "Expected AmvItemIdList, got $topSpaces" }
            new = original.copy(itemIds = original.itemIds + spaceId)
        } else {
            new = AmvItemIdList(owner = uuid7(), markerName = SpaceMarkers.TOP_SPACES, listOf(spaceId))
        }

        context += new
    }

}
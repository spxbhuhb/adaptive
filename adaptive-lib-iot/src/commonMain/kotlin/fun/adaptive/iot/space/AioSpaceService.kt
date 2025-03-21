package `fun`.adaptive.iot.space

import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.item.AioItem
import `fun`.adaptive.iot.item.AioMarker
import `fun`.adaptive.iot.item.AioStatus
import `fun`.adaptive.iot.space.markers.AmvSpace
import `fun`.adaptive.iot.space.markers.SpaceMarkers
import `fun`.adaptive.iot.value.AioValue
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.iot.value.AioValueWorker
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.runtime.GlobalRuntimeContext
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

    override suspend fun add(name: String, spaceType: AioMarker, parentId: AioValueId?): AioValueId {
        publicAccess()

        val spaceId = uuid7<AioValue>()

        worker.execute {

            val spaceSpec = AmvSpace(owner = spaceId, area = 0.0)

            val space = AioItem(
                name,
                AioWsContext.WSIT_SPACE + ":$spaceType",
                spaceId,
                now(),
                AioStatus.OK,
                nextFriendlyId(SpaceMarkers.SPACE, "SP-"),
                markersOrNull = mutableMapOf(
                    SpaceMarkers.SPACE to spaceSpec.uuid,
                    spaceType to null
                ),
                parentId = parentId
            )

            this += space
            this += spaceSpec

            if (parentId == null) {
                addTopList(spaceId, SpaceMarkers.TOP_SPACES)
            } else {
                addChild(parentId, spaceId, SpaceMarkers.SUB_SPACES)
            }
        }

        return spaceId
    }

    override suspend fun rename(spaceId: AioValueId, name: String) {
        publicAccess()

        worker.updateItem(spaceId) {
            it.copy(timestamp = now(), name = name)
        }
    }

    override suspend fun moveUp(spaceId: AioValueId) {
        publicAccess()

        worker.execute {
            moveUp(spaceId, SpaceMarkers.SUB_SPACES, SpaceMarkers.TOP_SPACES)
        }
    }

    override suspend fun moveDown(spaceId: AioValueId) {
        publicAccess()

        worker.execute {
            moveDown(spaceId, SpaceMarkers.SUB_SPACES, SpaceMarkers.TOP_SPACES)
        }
    }

    override suspend fun getSpaceData(spaceId: AioValueId): AmvSpace {
        publicAccess()

        return worker.markerVal(spaceId, SpaceMarkers.SPACE)
    }

    override suspend fun setSpaceData(valueId: AioValueId, area: Double, notes: String?) {
        publicAccess()

        return worker.update<AmvSpace>(valueId) {
            it.copy(timestamp = now(), area = area, notes = notes)
        }
    }

}
package `fun`.adaptive.iot.point

import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.item.AvStatus
import kotlinx.datetime.Clock.System.now

class AioPointService : AioPointApi, ServiceImpl<AioPointService> {

    companion object {
        lateinit var worker: AvValueWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isServer)
        worker = safeAdapter.firstImpl<AvValueWorker>()
    }

    override suspend fun add(
        name: String,
        itemType: AvMarker,
        parentId: AvValueId,
        spec : AioPointSpec
    ): AvValueId {
        publicAccess()

        val itemId = uuid7<AvValue>()

        worker.execute {

            val item = AvItem(
                name,
                AioWsContext.WSIT_POINT + ":$itemType",
                itemId,
                now(),
                AvStatus.OK,
                parentId,
                nextFriendlyId(PointMarkers.POINT, "PT-"),
                markersOrNull = mutableMapOf(
                    PointMarkers.POINT to null,
                    itemType to null
                ),
                specific = spec
            )

            this += item

            addChild(parentId, itemId, PointMarkers.POINTS)
        }

        return itemId
    }

    override suspend fun rename(spaceId: AvValueId, name: String) {
        publicAccess()

        worker.updateItem(spaceId) {
            it.copy(timestamp = now(), name = name)
        }
    }

    override suspend fun moveUp(spaceId: AvValueId) {
        publicAccess()

        worker.execute {
            moveUp(spaceId, SpaceMarkers.SUB_SPACES, SpaceMarkers.TOP_SPACES)
        }
    }

    override suspend fun moveDown(spaceId: AvValueId) {
        publicAccess()

        worker.execute {
            moveDown(spaceId, SpaceMarkers.SUB_SPACES, SpaceMarkers.TOP_SPACES)
        }
    }

    override suspend fun setSpec(valueId: AvValueId, spec : AioPointSpec) {
        publicAccess()

        return worker.update<AvItem<AioPointSpec>>(valueId) {
            it.copy(timestamp = now(), specific = spec)
        }
    }

    override suspend fun setCurVal(curVal : AvValue) {
        println(curVal)
    }


}
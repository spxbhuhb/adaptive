package `fun`.adaptive.iot.space

import `fun`.adaptive.auth.context.ensureLoggedIn
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.app.IoTValueDomain
import `fun`.adaptive.iot.device.ui.DeviceItems
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.value.*
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.util.serviceSubscribe
import kotlinx.datetime.Clock.System.now

class AioSpaceService : AioSpaceApi, ServiceImpl<AioSpaceService> {

    companion object {
        lateinit var worker: AvValueWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isServer)
        worker = safeAdapter.firstImpl<AvValueWorker> { it.domain == IoTValueDomain }
    }

    override suspend fun subscribe(subscriptionId: AvValueSubscriptionId): List<AvSubscribeCondition> {
        return serviceSubscribe(
            worker,
            subscriptionId,
            SpaceMarkers.SPACE,
            SpaceMarkers.TOP_SPACES,
            SpaceMarkers.SUB_SPACES
        )
    }

    override suspend fun unsubscribe(subscriptionId: AvValueSubscriptionId) {
       worker.unsubscribe(subscriptionId)
    }

    override suspend fun add(name: String, spaceType: AvMarker, parentId: AvValueId?): AvValueId {
        ensureLoggedIn()

        val spaceId = uuid7<AvValue>()

        worker.execute {

            val space = AvItem(
                name,
                DeviceItems.WSIT_SPACE + ":$spaceType",
                spaceId,
                now(),
                AvStatus.OK,
                parentId,
                nextFriendlyId(SpaceMarkers.SPACE, "SP-"),
                markersOrNull = mutableMapOf(
                    SpaceMarkers.SPACE to null,
                    spaceType to null
                ),
                spec = AioSpaceSpec(area = 0.0)
            )

            this += space

            if (parentId == null) {
                addTopList(spaceId, SpaceMarkers.TOP_SPACES)
            } else {
                addChild(parentId, spaceId, SpaceMarkers.SUB_SPACES)
            }
        }

        return spaceId
    }

    override suspend fun rename(spaceId: AvValueId, name: String) {
        ensureLoggedIn()

        worker.updateItem(spaceId) {
            it.copy(timestamp = now(), name = name)
        }
    }

    override suspend fun moveUp(spaceId: AvValueId) {
        ensureLoggedIn()

        worker.execute {
            moveUp(spaceId, SpaceMarkers.SUB_SPACES, SpaceMarkers.TOP_SPACES)
        }
    }

    override suspend fun moveDown(spaceId: AvValueId) {
        ensureLoggedIn()

        worker.execute {
            moveDown(spaceId, SpaceMarkers.SUB_SPACES, SpaceMarkers.TOP_SPACES)
        }
    }

    override suspend fun setSpecSpec(valueId: AvValueId, spec : AioSpaceSpec) {
        ensureLoggedIn()

        return worker.update<AvItem<AioSpaceSpec>>(valueId) {
            it.copy(timestamp = now(), spec = spec)
        }
    }

    override suspend fun setSpace(itemId: AvValueId, spaceId: AvValueId, listMarker : AvMarker) {
        ensureLoggedIn()

        worker.execute {
            addRef(itemId, SpaceMarkers.SPACE_REF, spaceId, listMarker)
        }
    }

}
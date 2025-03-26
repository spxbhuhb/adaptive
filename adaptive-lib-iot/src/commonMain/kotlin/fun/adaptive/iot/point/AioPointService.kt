package `fun`.adaptive.iot.point

import `fun`.adaptive.adat.AdatChange
import `fun`.adaptive.adat.api.deepCopy
import `fun`.adaptive.auth.context.publicAccess
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.history.AioHistoryService
import `fun`.adaptive.iot.point.computed.AioPointComputeWorker
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem
import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.item.AvStatus
import kotlinx.datetime.Clock.System.now

class AioPointService : AioPointApi, ServiceImpl<AioPointService> {

    companion object {
        lateinit var valueWorker: AvValueWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isServer)
        valueWorker = safeAdapter.firstImpl<AvValueWorker>()
    }

    override suspend fun add(
        name: String,
        itemType: AvMarker,
        parentId: AvValueId,
        spec: AioPointSpec,
        markers: Map<AvMarker, AvValueId?>?
    ): AvValueId {
        publicAccess()

        val itemId = markers?.get("migratedId")?.cast() ?: uuid7<AvValue>()

        val itemMarkers = markers?.toMutableMap() ?: mutableMapOf()

        itemMarkers[PointMarkers.POINT] = null
        itemMarkers[itemType] = null

        valueWorker.execute {

            val item = AvItem(
                name,
                AioWsContext.WSIT_POINT + ":$itemType",
                itemId,
                now(),
                AvStatus.OK,
                parentId,
                nextFriendlyId(PointMarkers.POINT, "PT-"),
                markersOrNull = itemMarkers,
                specific = spec
            )

            this += item

            addChild(parentId, itemId, PointMarkers.POINTS)
        }

        return itemId
    }

    override suspend fun rename(spaceId: AvValueId, name: String) {
        publicAccess()

        valueWorker.updateItem(spaceId) {
            it.copy(timestamp = now(), name = name)
        }
    }

    override suspend fun moveUp(spaceId: AvValueId) {
        publicAccess()

        valueWorker.execute {
            moveUp(spaceId, SpaceMarkers.SUB_SPACES, SpaceMarkers.TOP_SPACES)
        }
    }

    override suspend fun moveDown(spaceId: AvValueId) {
        publicAccess()

        valueWorker.execute {
            moveDown(spaceId, SpaceMarkers.SUB_SPACES, SpaceMarkers.TOP_SPACES)
        }
    }

    override suspend fun setSpec(valueId: AvValueId, spec: AioPointSpec) {
        publicAccess()

        return valueWorker.update<AvItem<AioPointSpec>>(valueId) {
            it.copy(timestamp = now(), specific = spec)
        }
    }

    override suspend fun setCurVal(curVal: AvValue) {
        publicAccess()

        val pointId = checkNotNull(curVal.parentId)

        val newCurVal = valueWorker.execute {
            unsafeSetCurVal(this, pointId, curVal)
        }

        AioPointComputeWorker.update(newCurVal)
        AioHistoryService.append(newCurVal)
    }

    internal fun unsafeSetCurVal(context: AvValueWorker.WorkerComputeContext, pointId: AvValueId, curVal: AvValue): AvValue {
        val point = context.item(pointId).asAvItem<AioPointSpec>()
        val originalCurValId = point.markers[PointMarkers.CUR_VAL]
        val curValId = originalCurValId ?: uuid7<AvValue>()

        val newCurVal = curVal.deepCopy(AdatChange(listOf("uuid"), curValId))

        context += newCurVal

        val markers = if (originalCurValId == null) {
            point.toMutableMarkers().also { it[PointMarkers.CUR_VAL] = curValId }
        } else {
            point.markersOrNull
        }

        context += point.copy(
            status = curVal.status,
            timestamp = curVal.timestamp,
            markersOrNull = markers
        )

        return newCurVal
    }
}
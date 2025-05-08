package `fun`.adaptive.iot.point

import `fun`.adaptive.adat.AdatChange
import `fun`.adaptive.adat.api.deepCopy
import `fun`.adaptive.auth.context.ensureLoggedIn
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.history.AioHistoryService
import `fun`.adaptive.iot.point.computed.AioPointComputeWorker
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.iot.app.WsItemTypes
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.store.AvComputeContext
import kotlinx.datetime.Clock.System.now

class AioPointService : AioPointApi, ServiceImpl<AioPointService>() {

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
        ensureLoggedIn()

        val itemId = markers?.get("migratedId")?.cast() ?: uuid7<AvValue>()

        val itemMarkers = markers?.toMutableMap() ?: mutableMapOf()

        itemMarkers[PointMarkers.POINT] = null
        itemMarkers[itemType] = null

        valueWorker.execute {

            val item = AvItem(
                name,
                WsItemTypes.WSIT_POINT + ":$itemType",
                itemId,
                now(),
                AvStatus.OK,
                parentId,
                nextFriendlyId(PointMarkers.POINT, "PT-"),
                markersOrNull = itemMarkers,
                spec = spec
            )

            this += item

            addChild(parentId, itemId, PointMarkers.POINTS)
        }

        return itemId
    }

    override suspend fun rename(valueId: AvValueId, name: String) {
        ensureLoggedIn()

        valueWorker.updateItem(valueId) {
            it.copy(timestamp = now(), name = name)
        }
    }

    override suspend fun moveUp(valueId: AvValueId) {
        ensureLoggedIn()

        valueWorker.execute {
            moveUp(valueId, SpaceMarkers.SUB_SPACES, SpaceMarkers.TOP_SPACES)
        }
    }

    override suspend fun moveDown(valueId: AvValueId) {
        ensureLoggedIn()

        valueWorker.execute {
            moveDown(valueId, SpaceMarkers.SUB_SPACES, SpaceMarkers.TOP_SPACES)
        }
    }

    override suspend fun setSpec(valueId: AvValueId, spec: AioPointSpec) {
        ensureLoggedIn()

        return valueWorker.update<AvItem<AioPointSpec>>(valueId) {
            it.copy(timestamp = now(), spec = spec)
        }
    }

    override suspend fun setCurVal(curVal: AvValue) {
        ensureLoggedIn()

        val pointId = checkNotNull(curVal.parentId)

        if (valueWorker[pointId] == null) {
            getLogger("AioPointService").warning("dropping curVal for unknown point: $pointId  $curVal")
            return
        }

        val newCurVal = valueWorker.execute {
            unsafeSetCurVal(this, pointId, curVal)
        }

        AioPointComputeWorker.update(newCurVal)
        AioHistoryService.append(newCurVal)
    }

    internal fun unsafeSetCurVal(context: AvComputeContext, pointId: AvValueId, curVal: AvValue): AvValue {
        val point = context.item<AioPointSpec>(pointId)
        val originalCurValId = point.markers[PointMarkers.CUR_VAL]
        val curValId = originalCurValId ?: uuid7()

        val curValWithId = curVal.deepCopy(AdatChange(listOf("uuid"), curValId))

        val newCurVal = point.spec.conversion?.convert(curValWithId) ?: curValWithId

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
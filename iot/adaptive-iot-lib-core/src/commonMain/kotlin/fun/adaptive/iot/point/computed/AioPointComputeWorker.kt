package `fun`.adaptive.iot.point.computed

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.history.AioHistoryService
import `fun`.adaptive.iot.point.AioPointService
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.utility.safeSuspendCall
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.builtin.AvConvertedDouble
import `fun`.adaptive.value.builtin.AvDouble
import `fun`.adaptive.value.item.AvItem.Companion.asAvItemOrNull
import kotlinx.coroutines.channels.Channel

class AioPointComputeWorker : WorkerImpl<AioPointComputeWorker> {

    companion object {
        suspend fun update(curVal: AvValue?) = curVal?.let { channel.send(curVal) }

        private val channel = Channel<AvValue>(Channel.UNLIMITED)
    }

    val valueWorker by worker<AvValueWorker>()

    override suspend fun run() {
        for (value in channel) {
            safeSuspendCall(logger) {
                val dependentPointIds = valueWorker.execute { collectDependents(value) ?: emptyList() }

                dependentPointIds.forEach { pointId ->
                    computePoint(pointId, value)
                }
            }
        }
    }

    fun AvValueWorker.WorkerComputeContext.collectDependents(value: AvValue): MutableList<AvValueId>? {
        val point = itemOrNull(value.parentId) ?: return null

        // point container may be a controller or a space

        val container = itemOrNull(point.parentId) ?: return null
        val containerMarkers = container.markersOrNull ?: return null

        // find out the space id this computation should work with

        val spaceId: AvValueId?

        when {
            DeviceMarkers.CONTROLLER in containerMarkers -> spaceId = containerMarkers[SpaceMarkers.SPACE_REF]
            SpaceMarkers.SPACE in containerMarkers -> spaceId = container.parentId
            else -> spaceId = null
        }

        if (spaceId == null) return null

        // here we have a space id for this value, let's get the space

        val space = itemOrNull(spaceId) ?: return null
        val spacePointIds = safeItemIds(space.markersOrNull?.get(PointMarkers.POINTS))

        // We have a safe list of space point ids, let's try to find a point which
        // is interested in this value.

        val pointsToCompute = mutableListOf<AvValueId>()

        for (spacePointId in spacePointIds) {
            val spacePoint = itemOrNull(spacePointId)?.asAvItemOrNull<AioComputedPointSpec>() ?: continue

            val dependencyMarker = spacePoint.spec.dependencyMarker
            if (dependencyMarker !in point.markers) continue

            pointsToCompute += spacePointId
        }

        return pointsToCompute
    }

    suspend fun computePoint(computedPoint: AvValueId, incomingValue: AvValue) {

        val value = when (incomingValue) {
            is AvConvertedDouble -> incomingValue.convertedValue
            is AvDouble -> incomingValue.value
            else -> return
        }

        val parentId = incomingValue.parentId ?: return

        val sourceValues = sourceMaps.getOrPut(computedPoint) { mutableMapOf() }
        sourceValues[parentId] = value

        val result = sourceValues.values.sum() / sourceValues.size

        val newCurVal = valueWorker.execute {
            AioPointService().unsafeSetCurVal(this, computedPoint, AvDouble(computedPoint, result))
        }

        update(newCurVal)
        newCurVal?.let { AioHistoryService.append(newCurVal) }
    }


    val sourceMaps = mutableMapOf<AvValueId, MutableMap<AvValueId, Double>>()
}
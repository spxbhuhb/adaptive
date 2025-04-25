package `fun`.adaptive.iot.point.computed

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.chart.model.ChartDataRange.Companion.max
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.history.AioHistoryService
import `fun`.adaptive.iot.point.AioPointService
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.iot.space.AioSpaceSpec
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.utility.safeSuspendCall
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.builtin.AvConvertedDouble
import `fun`.adaptive.value.builtin.AvDouble
import `fun`.adaptive.value.item.AvItem.Companion.asAvItemOrNull
import `fun`.adaptive.value.store.AvComputeContext
import kotlinx.coroutines.channels.Channel
import kotlinx.datetime.Instant

class AioPointComputeWorker : WorkerImpl<AioPointComputeWorker> {

    companion object {
        suspend fun update(curVal: AvValue?) = curVal?.let { channel.send(curVal) }

        private val channel = Channel<AvValue>(Channel.UNLIMITED)
    }

    val valueWorker by worker<AvValueWorker>()

    val sourceMaps = mutableMapOf<AvValueId, MutableMap<AvValueId, Double>>()

    val timestamps = mutableMapOf<AvValueId, Instant>()

    override suspend fun run() {
        for (value in channel) {
            safeSuspendCall(logger) {

                val pointsToCompute = mutableListOf<AvValueId>()
                val space = valueWorker.execute { collectDependents(value, pointsToCompute) }

                if (space != null) {
                    pointsToCompute.forEach { pointId ->
                        computePoint(pointId, value)
                    }
                    valueWorker.execute {
                        val curSpace = item<AioSpaceSpec>(space.uuid)
                        if (curSpace.timestamp < value.timestamp) {
                            this += curSpace.copy(timestamp = value.timestamp)
                        }
                    }
                }
            }
        }
    }

    fun AvComputeContext.collectDependents(
        value: AvValue,
        pointsToCompute: MutableList<AvValueId>
    ): AvValue? {

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

        for (spacePointId in spacePointIds) {
            val spacePoint = itemOrNull(spacePointId)?.asAvItemOrNull<AioComputedPointSpec>() ?: continue

            val dependencyMarker = spacePoint.spec.dependencyMarker
            if (dependencyMarker !in point.markers) continue

            pointsToCompute += spacePointId
        }

        return space
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

        val timestamp = timestamps.getOrPut(computedPoint) { incomingValue.timestamp }

        val resultTimestamp = max(timestamp, incomingValue.timestamp)
        if (timestamp < incomingValue.timestamp) timestamps[computedPoint] = resultTimestamp

        val resultValue = sourceValues.values.sum() / sourceValues.size

        val newCurVal = valueWorker.execute {
            AioPointService().unsafeSetCurVal(this, computedPoint, AvDouble(computedPoint, resultValue, resultTimestamp))
        }

        update(newCurVal)
        newCurVal.let { AioHistoryService.append(newCurVal) }
    }

}
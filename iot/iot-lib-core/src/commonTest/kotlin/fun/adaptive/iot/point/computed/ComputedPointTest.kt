package `fun`.adaptive.iot.point.computed

import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.haystack.PhScienceMarkers
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.iot.test.TestControllerSpec
import `fun`.adaptive.iot.test.TestNetworkSpec
import `fun`.adaptive.iot.test.TestPointSpec
import `fun`.adaptive.iot.test.TestSupport
import `fun`.adaptive.iot.test.TestSupport.Companion.serverTest
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.clearedTestPath
import `fun`.adaptive.utility.waitFor
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.builtin.AvDouble
import `fun`.adaptive.value.item.AvStatus
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.seconds

class ComputedPointTest {

    @Test
    fun basic() = serverTest(clearedTestPath()) {
        val spaceId = spaceService.add("test-room", SpaceMarkers.ROOM, null)

        val computedId = pointService.add(
            "comp-temp",
            PointMarkers.COMPUTED_POINT,
            spaceId,
            AioComputedPointSpec(dependencyMarker = PhScienceMarkers.TEMP),
            mapOf(PhScienceMarkers.TEMP to null, PointMarkers.HIS to null)
        )

        val networkId = deviceService.add("test-network", DeviceMarkers.NETWORK, null, TestNetworkSpec())
        val controllerId = deviceService.add("test-controller", DeviceMarkers.CONTROLLER, networkId, TestControllerSpec())
        val point1Id = pointService.add("test-point1", PointMarkers.POINT, controllerId, TestPointSpec(), markers = mapOf(PhScienceMarkers.TEMP to null))

        spaceService.setSpace(controllerId, spaceId, SpaceMarkers.SPACE_DEVICES)

        delay(10)

        var timestamp = setCurVal(point1Id, 10.0)
        waitForCurVal(timestamp, computedId)
        assertCurVal(10.0, timestamp, computedId)

        delay(10)

        timestamp = setCurVal(point1Id, 11.0)
        waitForCurVal(timestamp, computedId)
        assertCurVal(11.0, timestamp, computedId)

        val space = serverValueWorker.item(spaceId)
        assertEquals(timestamp, space.timestamp)
    }

    suspend fun TestSupport.setCurVal(pointId: AvValueId, value: Double): Instant {
        val timestamp = now()
        val curVal = AvDouble(UUID.nil(), timestamp, AvStatus.OK, pointId, value)
        pointService.setCurVal(curVal)
        waitForCurVal(curVal.timestamp, pointId)
        return timestamp
    }

    suspend fun TestSupport.waitForCurVal(timestamp: Instant, pointId: AvValueId) {
        waitFor(500.seconds) {
            val point = serverValueWorker.item(pointId)
            if (PointMarkers.CUR_VAL !in point.markers) return@waitFor false
            val curVal = serverValueWorker[point.markers[PointMarkers.CUR_VAL] !!]
            assertIs<AvDouble>(curVal)
            curVal.lastChange >= timestamp
        }
    }

    fun TestSupport.assertCurVal(expected: Double, timestamp: Instant, pointId: AvValueId) {
        val newComputed = serverValueWorker.item(pointId)
        val newCurVal = serverValueWorker[newComputed.markers[PointMarkers.CUR_VAL] !!]

        assertIs<AvDouble>(newCurVal)
        assertEquals(expected, newCurVal.value)
        assertEquals(timestamp, newCurVal.lastChange)
    }

}
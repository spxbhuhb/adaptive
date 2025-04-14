package `fun`.adaptive.iot.driver.backend

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.driver.request.*
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvItem.Companion.withSpec
import kotlin.reflect.KClass

class AioDriverWorker<NT : AioDeviceSpec,CT: AioDeviceSpec,PT: AioPointSpec>(
    val plugin: AioProtocolPlugin<NT,CT,PT>,
    val networkSpecClass : KClass<NT>,
    val controllerSpecClass : KClass<CT>,
    val pointSpecClass : KClass<PT>
) : WorkerImpl<AioDriverWorker<*,*,*>> {

    val valueWorker by worker<AvValueWorker>()

    override suspend fun run() {
        plugin.valueWorker = valueWorker
    }

    suspend fun commissionNetwork(request: AdrCommissionNetwork<*>) {
        val new = request.item.withSpec(networkSpecClass)
        valueWorker.execute {
            val original = queryByMarker(DeviceMarkers.NETWORK).singleOrNull()?.withSpec(networkSpecClass)
            check(original == null || original.uuid == new.uuid) { "uuid mismatch: ${original!!.uuid} != ${new.uuid}"}

            plugin.commissionNetwork(this, original, new)

            if (original == null) {
                logger.info { "initial network commissioning: $new" }
            } else {
                logger.info { "network change: $new" }
            }
        }
    }

    suspend fun commissionController(request: AdrCommissionController<*>) {
        plugin.commissionController(request.item.withSpec(controllerSpecClass))
    }

    suspend fun commissionPoint(request: AdrCommissionPoint<*>) {
        plugin.commissionPoint(request.item.withSpec(pointSpecClass))
    }

    suspend fun startControllerDiscovery(request: AdrStartControllerDiscovery) {
        TODO("Not yet implemented")
    }

    suspend fun disable(request: AdrDisable) {
        val item = valueWorker.item(request.subjectId)
        val markers = item.markers
        when {
            DeviceMarkers.NETWORK in markers -> plugin.disableNetwork(item.withSpec(networkSpecClass))
            DeviceMarkers.CONTROLLER in markers -> plugin.disableController(item.withSpec(controllerSpecClass))
            PointMarkers.POINT in markers -> plugin.disablePoint(item.withSpec(pointSpecClass))
        }
    }

    suspend fun enable(request: AdrEnable) {
        val item = valueWorker.item(request.subjectId)
        val markers = item.markers
        when {
            DeviceMarkers.NETWORK in markers -> plugin.enableNetwork(item.withSpec(networkSpecClass))
            DeviceMarkers.CONTROLLER in markers -> plugin.enableController(item.withSpec(controllerSpecClass))
            PointMarkers.POINT in markers -> plugin.enablePoint(item.withSpec(pointSpecClass))
        }
    }

    suspend fun writePoint(request: AdrWritePoint<*>) {
        TODO("Not yet implemented")
    }

    suspend fun readPoint(request: AdrReadPoint) {
        TODO("Not yet implemented")
    }

    suspend fun ping(request: AdrPing) {
        TODO("Not yet implemented")
    }

    suspend fun startTrace(request: AdrStartTrace) {

    }

    suspend fun stopTrace(request: AdrStopTrace) {
        TODO("Not yet implemented")
    }


}
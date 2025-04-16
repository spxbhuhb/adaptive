package `fun`.adaptive.iot.driver.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.encodeToJsonString
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.iot.device.AioDeviceSpec
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.iot.driver.request.*
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.lib.util.bytearray.ByteArrayQueue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.withSpec
import kotlinx.io.files.Path
import kotlin.reflect.KClass

class AioDriverWorker<NT : AioDeviceSpec, CT : AioDeviceSpec, PT : AioPointSpec>(
    val plugin: AioProtocolPlugin<NT, CT, PT>,
    announcementQueuePath: Path,
    val networkSpecClass: KClass<NT>,
    val controllerSpecClass: KClass<CT>,
    val pointSpecClass: KClass<PT>
) : WorkerImpl<AioDriverWorker<*, *, *>> {

    val valueWorker by worker<AvValueWorker>()

    val announcementQueue = ByteArrayQueue(
        announcementQueuePath,
        chunkSizeLimit = 1024*1024,
        barrier = byteArrayOf(0x0A),
        persistDequeue = true,
        textual = true
    )

    override suspend fun run() {
        plugin.driverWorker = this
        plugin.valueWorker = valueWorker
        plugin.start()
    }

    override fun unmount() {
        plugin.stop()
        super.unmount()
    }

    fun history(data: AdatClass, message: () -> String) {
        logger.info { "${message()} ${data.encodeToJsonString()}" }
    }

    fun history(new: AdatClass, original : AdatClass, message: () -> String) {
        logger.info { "${message()} new: ${new.encodeToJsonString()} original: ${original.encodeToJsonString()}" }
    }

    suspend fun commissionNetwork(request: AdrCommissionNetwork<*>) {
        val new = request.item.withSpec(networkSpecClass)

        valueWorker.execute {
            val original = queryByMarker(DeviceMarkers.NETWORK).singleOrNull()?.withSpec(networkSpecClass)
            check(original == null || original.uuid == new.uuid) { "uuid mismatch: ${original !!.uuid} != ${new.uuid}" }

            plugin.commissionNetwork(this, original, new)

            if (original == null) {
                history(new) { "initial network commissioning" }
            } else {
                history(new, original) { "network change" }
            }
        }

    }

    suspend fun commissionController(request: AdrCommissionController<*>) {
        val new = request.item.withSpec(controllerSpecClass)

        valueWorker.execute {
            val original = get(new.uuid)?.withSpec(controllerSpecClass)

            plugin.commissionController(this, original, new)

            if (original == null) {
                history(new) { "initial controller commissioning" }
            } else {
                history(new, original) { "controller change" }
            }
        }

    }

    suspend fun commissionPoint(request: AdrCommissionPoint<*>) {
        val new = request.item.withSpec(pointSpecClass)

        valueWorker.execute {
            val original = get(new.uuid)?.withSpec(pointSpecClass)

            plugin.commissionPoint(this, original, new)

            if (original == null) {
                history(new) { "initial point commissioning" }
            } else {
                history(new, original) { "point change" }
            }
        }

    }

    fun startControllerDiscovery(request: AdrStartControllerDiscovery) {
        plugin.startControllerDiscovery(request)
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
        val (controller, point) = controllerAndPoint(request.pointId)
        plugin.writePoint(controller, point, request.value)
    }

    suspend fun readPoint(request: AdrReadPoint) {
        val (controller, point) = controllerAndPoint(request.pointId)
        plugin.readPoint(controller, point)
    }

    suspend fun ping(request: AdrPing) {
        val controller = valueWorker.item(request.controllerId).withSpec(controllerSpecClass)
        plugin.ping(controller)
    }

    suspend fun startTrace(request: AdrStartTrace) {

    }

    suspend fun stopTrace(request: AdrStopTrace) {

    }

    fun controllerAndPoint(pointId : AvValueId) : Pair<AvItem<CT>, AvItem<PT>> {
        val point = valueWorker.item(pointId).withSpec(pointSpecClass)
        val controllerId = checkNotNull(point.parentId) { "point ${point.uuid} has no parent controller" }
        val controller = valueWorker.item(controllerId).withSpec(controllerSpecClass)
        return controller to point
    }
}
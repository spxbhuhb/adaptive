package `fun`.adaptive.iot.device

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.iot.common.AioMarkerSet
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use

class AioDeviceWorker : WorkerImpl<AioDeviceWorker> {

    private val lock = getLock()

    private val devices = mutableMapOf<AioDeviceId, AioDevice>()

    override suspend fun run() {

    }

    fun query(markerSet: AioMarkerSet): List<AioDevice> =
        lock.use {
            if (markerSet.isEmpty()) {
                devices.values.toList()
            } else {
                devices.values.filter { it.markers.containsAll(markerSet) }
            }
        }

    fun add(device: AioDevice) {
        lock.use {
            check(device.uuid !in devices) { "Device with uuid ${device.uuid} already exists" }
            devices[device.uuid] = device
        }
    }

    fun update(device: AioDevice) {
        lock.use {
            check(device.uuid in devices) { "Device with uuid ${device.uuid} does not exist" }
            devices[device.uuid] = device
        }
    }

    fun remove(device: AioDevice) {
        lock.use {
            devices.remove(device.uuid)
        }
    }

}
package `fun`.adaptive.iot.app

import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.iot.device.AioDeviceService
import `fun`.adaptive.iot.space.AioSpaceService

@Adaptive
fun iotServerBackend() {
    
    service { AioSpaceService() }
    service { AioDeviceService() }

}
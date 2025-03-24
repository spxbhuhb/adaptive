package `fun`.adaptive.iot.app

import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.iot.device.AioDeviceService
import `fun`.adaptive.iot.history.AioHistoryService
import `fun`.adaptive.iot.history.backend.AioHistoryWorker
import `fun`.adaptive.iot.point.AioPointService
import `fun`.adaptive.iot.point.computed.AioPointComputeWorker
import `fun`.adaptive.iot.space.AioSpaceService

@Adaptive
fun iotServerBackend() {
    
    service { AioSpaceService() }
    service { AioDeviceService() }

    service { AioPointService() }
    worker { AioPointComputeWorker() }

    service { AioHistoryService() }
    worker { AioHistoryWorker() }

}
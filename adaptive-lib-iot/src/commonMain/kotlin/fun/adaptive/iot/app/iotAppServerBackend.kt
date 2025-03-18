package `fun`.adaptive.iot.app

import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.iot.value.AioValueServerService
import `fun`.adaptive.iot.value.AioValueWorker
import `fun`.adaptive.iot.infrastructure.AioInfrastructureService
import `fun`.adaptive.iot.infrastructure.network.AioNetworkService
import `fun`.adaptive.iot.infrastructure.network.AioNetworkWorker
import `fun`.adaptive.iot.space.SpaceService

@Adaptive
fun iotAppServerMain() {

    service { SpaceService() }

    worker { AioValueWorker() }
    service { AioValueServerService() }

    worker { AioDeviceWorker() }
    service { AioDeviceService() }

    worker { AioNetworkWorker() }
    service { AioNetworkService() }

    service { AioInfrastructureService() }
}
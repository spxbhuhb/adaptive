package `fun`.adaptive.iot.app

import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.iot.value.AioValueServerService
import `fun`.adaptive.iot.value.AioValueWorker

@Adaptive
fun iotAppServerMain() {
    
    worker { AioValueWorker() }
    service { AioValueServerService() }

}
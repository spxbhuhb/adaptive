package `fun`.adaptive.iot.app

import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.iot.space.AioSpaceService
import `fun`.adaptive.iot.value.AioValueServerService
import `fun`.adaptive.iot.value.AioValueWorker
import `fun`.adaptive.iot.value.persistence.AbstractValuePersistence
import `fun`.adaptive.iot.value.persistence.NoPersistence

@Adaptive
fun iotAppServerMain(
    persistence : AbstractValuePersistence = NoPersistence()
) {
    
    worker { AioValueWorker(persistence) }
    service { AioValueServerService() }
    service { AioSpaceService() }

}
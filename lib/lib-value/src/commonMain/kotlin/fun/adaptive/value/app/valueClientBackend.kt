package `fun`.adaptive.value.app

import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.value.AvValueClientService
import `fun`.adaptive.value.AvValueWorker

@Adaptive
fun valueClientBackend() {

    worker { AvValueWorker(proxy = true) }
    service { AvValueClientService() }

}
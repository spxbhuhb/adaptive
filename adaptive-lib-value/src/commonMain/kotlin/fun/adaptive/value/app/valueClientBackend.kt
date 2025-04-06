package `fun`.adaptive.value.app

import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.value.AvValueClientService
import `fun`.adaptive.value.AvValueDomain
import `fun`.adaptive.value.AvValueWorker

@Adaptive
fun valueClientBackend(domain: AvValueDomain) {

    worker { AvValueWorker(domain) }
    service { AvValueClientService() }

}
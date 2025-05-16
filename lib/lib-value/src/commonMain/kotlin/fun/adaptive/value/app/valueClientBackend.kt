package `fun`.adaptive.value.app

import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.workerImpl
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.value.AvValueClientService
import `fun`.adaptive.value.AvValueDomain
import `fun`.adaptive.value.AvValueWorker

@Adaptive
fun valueClientBackend(domain: AvValueDomain) {

    workerImpl { AvValueWorker(domain, proxy = true) }
    service { AvValueClientService() }

}
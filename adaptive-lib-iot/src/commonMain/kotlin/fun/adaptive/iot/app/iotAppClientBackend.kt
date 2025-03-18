package `fun`.adaptive.iot.app

import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.iot.curval.CurValClientService
import `fun`.adaptive.iot.curval.CurValWorker

@Adaptive
fun iotAppClientBackend() {

    worker { CurValWorker() }
    service { CurValClientService() }

}
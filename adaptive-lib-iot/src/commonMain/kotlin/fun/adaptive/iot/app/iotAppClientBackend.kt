package `fun`.adaptive.iot.app

import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.iot.curval.CurValClientService
import `fun`.adaptive.iot.curval.CurValServerService
import `fun`.adaptive.iot.curval.CurValWorker
import `fun`.adaptive.iot.device.AioDeviceService
import `fun`.adaptive.iot.device.AioDeviceWorker
import `fun`.adaptive.iot.network.AioNetworkService
import `fun`.adaptive.iot.network.AioNetworkWorker
import `fun`.adaptive.iot.space.SpaceService

@Adaptive
fun iotAppClientBackend() {

    worker { CurValWorker() }
    service { CurValClientService() }

}
package `fun`.adaptive.iot.app

import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.iot.backend.SpaceService

@Adaptive
fun iotAppServerMain() {
    service { SpaceService() }
}
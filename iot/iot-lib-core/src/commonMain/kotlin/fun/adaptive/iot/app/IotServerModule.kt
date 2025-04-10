package `fun`.adaptive.iot.app

import `fun`.adaptive.iot.alarm.AioAlarmService
import `fun`.adaptive.iot.device.AioDeviceService
import `fun`.adaptive.iot.device.publish.AioDeviceTreePublishService
import `fun`.adaptive.iot.domain.rht.publisher.RhtPublishService
import `fun`.adaptive.iot.history.AioHistoryService
import `fun`.adaptive.iot.history.backend.AioHistoryWorker
import `fun`.adaptive.iot.point.AioPointService
import `fun`.adaptive.iot.point.computed.AioPointComputeWorker
import `fun`.adaptive.iot.space.AioSpaceService
import `fun`.adaptive.iot.space.publish.AioSpaceTreePublishService
import `fun`.adaptive.runtime.ServerWorkspace

class IotServerModule<WT : ServerWorkspace> : IotModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        + AioSpaceTreePublishService()
        + AioSpaceService()

        + AioDeviceTreePublishService()
        + AioDeviceService()

        + AioPointService()
        + AioPointComputeWorker()
        + AioAlarmService()
        + AioHistoryService()
        + AioHistoryWorker()

        + RhtPublishService()
    }

}
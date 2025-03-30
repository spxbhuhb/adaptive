package `fun`.adaptive.iot.app

import `fun`.adaptive.iot.alarm.AioAlarmService
import `fun`.adaptive.iot.device.AioDeviceService
import `fun`.adaptive.iot.history.AioHistoryService
import `fun`.adaptive.iot.history.backend.AioHistoryWorker
import `fun`.adaptive.iot.point.AioPointService
import `fun`.adaptive.iot.point.computed.AioPointComputeWorker
import `fun`.adaptive.iot.space.AioSpaceService
import `fun`.adaptive.runtime.ServerWorkspace

class IotServerModule<WT : ServerWorkspace> : IotModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        + AioSpaceService()
        + AioDeviceService()
        + AioPointService()
        + AioPointComputeWorker()
        + AioAlarmService()
        + AioHistoryService()
        + AioHistoryWorker()
    }

}
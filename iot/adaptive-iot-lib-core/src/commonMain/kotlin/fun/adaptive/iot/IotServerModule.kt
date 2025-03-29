package `fun`.adaptive.iot

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.iot.alarm.AioAlarmService
import `fun`.adaptive.iot.device.AioDeviceService
import `fun`.adaptive.iot.history.AioHistoryService
import `fun`.adaptive.iot.history.backend.AioHistoryWorker
import `fun`.adaptive.iot.point.AioPointService
import `fun`.adaptive.iot.point.computed.AioPointComputeWorker
import `fun`.adaptive.iot.space.AioSpaceService
import `fun`.adaptive.runtime.ServerWorkspace

class IotServerModule<WT : ServerWorkspace> : IotModule<WT>() {

    override fun backendAdapterInit(adapter : BackendAdapter) = with(adapter){

    }

    //         service { AioSpaceService() }
    //        service { AioDeviceService() }
    //
    //        service { AioPointService() }
    //        worker { AioPointComputeWorker() }
    //
    //        service { AioAlarmService() }
    //
    //        service { AioHistoryService() }
    //        worker { AioHistoryWorker() }
}
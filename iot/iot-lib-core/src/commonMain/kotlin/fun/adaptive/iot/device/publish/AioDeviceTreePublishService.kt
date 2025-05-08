package `fun`.adaptive.iot.device.publish

import `fun`.adaptive.auth.context.ensureLoggedIn
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.app.IoTValueDomain
import `fun`.adaptive.iot.device.DeviceMarkers
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValueSubscriptionId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.util.serviceSubscribe

class AioDeviceTreePublishService : AioDeviceTreePublishApi, ServiceImpl<AioDeviceTreePublishService>() {

    companion object {
        lateinit var worker: AvValueWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isServer)
        worker = safeAdapter.firstImpl<AvValueWorker> { it.domain == IoTValueDomain }
    }

    override suspend fun subscribe(subscriptionId: AvValueSubscriptionId): List<AvSubscribeCondition> {
        ensureLoggedIn()

        return serviceSubscribe(
            worker,
            subscriptionId,
            DeviceMarkers.DEVICE,
            DeviceMarkers.TOP_DEVICES,
            DeviceMarkers.SUB_DEVICES
        )
    }

    override suspend fun unsubscribe(subscriptionId: AvValueSubscriptionId) {
        ensureLoggedIn()

        worker.unsubscribe(subscriptionId)
    }

}
package `fun`.adaptive.iot.space.publish

import `fun`.adaptive.auth.context.ensureLoggedIn
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.app.IoTValueDomain
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.value.*
import `fun`.adaptive.value.util.serviceSubscribe

class AioSpaceTreePublishService : AioSpaceTreePublishApi, ServiceImpl<AioSpaceTreePublishService> {

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
            SpaceMarkers.SPACE,
            SpaceMarkers.TOP_SPACES,
            SpaceMarkers.SUB_SPACES
        )
    }

    override suspend fun unsubscribe(subscriptionId: AvValueSubscriptionId) {
        ensureLoggedIn()

        worker.unsubscribe(subscriptionId)
    }

}
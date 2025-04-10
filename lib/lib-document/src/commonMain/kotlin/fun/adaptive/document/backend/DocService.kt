package `fun`.adaptive.document.backend

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.document.app.DocumentValueDomain
import `fun`.adaptive.document.api.DocApi
import `fun`.adaptive.document.value.DocMarkers
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValueSubscriptionId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.util.serviceSubscribe

class DocService : ServiceImpl<DocService>, DocApi {

    companion object {
        lateinit var worker: AvValueWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isServer)
        worker = safeAdapter.firstImpl<AvValueWorker> { it.domain == DocumentValueDomain }
    }

    override suspend fun subscribe(subscriptionId: AvValueSubscriptionId): List<AvSubscribeCondition> {
        return serviceSubscribe(
            worker,
            subscriptionId,
            DocMarkers.DOCUMENT,
            DocMarkers.TOP_DOCUMENTS,
            DocMarkers.SUB_DOCUMENTS
        )
    }

    override suspend fun unsubscribe(subscriptionId: AvValueSubscriptionId) {
        worker.unsubscribe(subscriptionId)
    }

}
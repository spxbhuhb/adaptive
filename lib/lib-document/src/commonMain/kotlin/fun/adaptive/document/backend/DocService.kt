package `fun`.adaptive.document.backend

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.document.api.DocApi
import `fun`.adaptive.document.value.DocMarkers
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvSubscriptionId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.util.serviceSubscribe

class DocService : ServiceImpl<DocService>(), DocApi {


    val worker: AvValueWorker by workerImpl<AvValueWorker>()

    override suspend fun subscribe(subscriptionId: AvSubscriptionId): List<AvSubscribeCondition> {
        return serviceSubscribe(
            worker,
            subscriptionId,
            DocMarkers.DOCUMENT,
            DocMarkers.TOP_DOCUMENTS,
            DocMarkers.SUB_DOCUMENTS
        )
    }

    override suspend fun unsubscribe(subscriptionId: AvSubscriptionId) {
        worker.unsubscribe(subscriptionId)
    }

}
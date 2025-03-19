package `fun`.adaptive.iot.value

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.iot.item.AioMarker
import `fun`.adaptive.iot.item.AioMarkerValue
import `fun`.adaptive.iot.value.operation.AioValueOperation

class AioValueTestClientService : ServiceImpl<AioValueTestClientService>, AioValueApi {

    override suspend fun process(operation: AioValueOperation) {
        adapter !!.firstImpl<AioValueWorker>().process(operation)
    }

    override suspend fun subscribe(valueIds: List<AioValueId>, markers: List<AioMarker>): AuiValueSubscriptionId {
        throw UnsupportedOperationException()
    }

    override suspend fun unsubscribe(subscriptionId: AuiValueSubscriptionId) {
        throw UnsupportedOperationException()
    }

}
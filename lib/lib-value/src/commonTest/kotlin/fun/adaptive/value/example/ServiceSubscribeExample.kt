package `fun`.adaptive.value.example

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvSubscriptionId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.util.serviceSubscribe

@ServiceApi
interface ExampleApi {
    suspend fun serviceFunForSubscribe(subscriptionId: AvSubscriptionId): List<AvSubscribeCondition>
    suspend fun serviceFunForUnsubscribe(subscriptionId: AvSubscriptionId)
}

class ServiceSubscribeExample : ServiceImpl<ServiceSubscribeExample>(), ExampleApi {

    val valueWorker by workerImpl<AvValueWorker>()

    override suspend fun serviceFunForSubscribe(subscriptionId: AvSubscriptionId): List<AvSubscribeCondition> {
        // perform the authorization here, if you use `lib-auth` then call `ensureLoggedIn`, for example
        return serviceSubscribe(valueWorker, uuid4(), "testMarker")
    }

    override suspend fun serviceFunForUnsubscribe(subscriptionId: AvSubscriptionId) {
        // perform the authorization here, if you use `lib-auth` then call `ensureLoggedIn`, for example
        valueWorker.unsubscribe(subscriptionId)
    }
}
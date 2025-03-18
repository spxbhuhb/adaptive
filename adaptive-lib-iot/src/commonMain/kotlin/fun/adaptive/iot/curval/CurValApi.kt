package `fun`.adaptive.iot.curval

import `fun`.adaptive.service.ServiceApi

@ServiceApi
interface CurValApi {

    suspend fun update(curVal: CurVal)

    suspend fun subscribe(valueIds : List<AioValueId>) : CurValSubscriptionId

    suspend fun unsubscribe(subscriptionId : CurValSubscriptionId)

}
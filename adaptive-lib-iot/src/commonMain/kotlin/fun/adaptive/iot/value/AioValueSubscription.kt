package `fun`.adaptive.iot.value

abstract class AioValueSubscription(
    val uuid: AuiValueSubscriptionId,
    val valueIds: List<AioValueId>
) {

    var worker: AioValueWorker? = null

    abstract fun update(value: AioValue)

}
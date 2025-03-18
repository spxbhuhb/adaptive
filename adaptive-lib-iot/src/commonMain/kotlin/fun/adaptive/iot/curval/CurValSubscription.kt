package `fun`.adaptive.iot.curval

abstract class CurValSubscription(
    val uuid: CurValSubscriptionId,
    val valueIds: List<AioValueId>
) {

    var worker : CurValWorker? = null

    abstract fun update(curVal: CurVal)

}
package `fun`.adaptive.value

fun interface AvSubscribeFun {
    suspend operator fun invoke(service : AvValueApi, subscriptionId: AvSubscriptionId) : List<AvSubscribeCondition>
}
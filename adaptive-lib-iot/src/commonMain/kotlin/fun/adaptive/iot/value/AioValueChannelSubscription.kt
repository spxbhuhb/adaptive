package `fun`.adaptive.iot.value

import `fun`.adaptive.iot.value.operation.AioValueOperation
import kotlinx.coroutines.channels.Channel

class AioValueChannelSubscription(
    uuid: AioValueSubscriptionId,
    conditions : List<AioSubscribeCondition>,
    val channel: Channel<AioValueOperation>
) : AioValueSubscription(uuid, conditions) {

    override fun send(value: AioValueOperation) {
        channel.trySend(value)
    }

}
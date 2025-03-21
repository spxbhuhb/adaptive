package `fun`.adaptive.value

import `fun`.adaptive.value.operation.AvValueOperation
import kotlinx.coroutines.channels.Channel

class AvChannelSubscription(
    uuid: AvValueSubscriptionId,
    conditions: List<AvSubscribeCondition>,
    val channel: Channel<AvValueOperation>
) : AvSubscription(uuid, conditions) {

    override fun send(value: AvValueOperation) {
        channel.trySend(value)
    }

}
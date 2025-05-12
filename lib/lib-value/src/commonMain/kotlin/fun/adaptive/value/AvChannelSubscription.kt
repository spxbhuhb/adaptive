package `fun`.adaptive.value

import `fun`.adaptive.value.operation.AvValueOperation
import kotlinx.coroutines.channels.Channel

class AvChannelSubscription(
    uuid: AvSubscriptionId,
    conditions: List<AvSubscribeCondition>,
    val channel: Channel<AvValueOperation>
) : AvSubscription(uuid, conditions) {

    override fun send(operation: AvValueOperation) {
        channel.trySend(operation)
    }

}
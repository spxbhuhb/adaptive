package `fun`.adaptive.value

import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.value.operation.AvValueOperation
import kotlinx.coroutines.channels.Channel

class AvChannelSubscription(
    uuid: AvValueSubscriptionId,
    conditions: List<AvSubscribeCondition>,
    val channel: Channel<AvValueOperation>
) : AvSubscription(uuid, conditions) {

    constructor(
        channel : Channel<AvValueOperation>,
        vararg conditions: AvSubscribeCondition,
    ) : this(uuid4(), conditions.toList(), channel)

    override fun send(value: AvValueOperation) {
        channel.trySend(value)
    }

}
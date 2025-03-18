package `fun`.adaptive.iot.value

import kotlinx.coroutines.channels.Channel

class AioValueChannelSubscription(
    uuid: AuiValueSubscriptionId,
    valueIds: List<AioValueId>,
    val channel: Channel<AioValue>
) : AioValueSubscription(uuid, valueIds) {

    override fun update(value: AioValue) {
        channel.trySend(value)
    }

}
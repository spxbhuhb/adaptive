package `fun`.adaptive.iot.value

import `fun`.adaptive.iot.item.AioMarker
import `fun`.adaptive.iot.value.operation.AioValueOperation
import kotlinx.coroutines.channels.Channel

class AioValueChannelSubscription(
    uuid: AuiValueSubscriptionId,
    valueIds: List<AioValueId>,
    markers : List<AioMarker>,
    val channel: Channel<AioValueOperation>
) : AioValueSubscription(uuid, valueIds, markers) {

    override fun send(value: AioValueOperation) {
        channel.trySend(value)
    }

}
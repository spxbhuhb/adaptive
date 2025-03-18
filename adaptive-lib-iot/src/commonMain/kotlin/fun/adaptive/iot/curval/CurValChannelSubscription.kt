package `fun`.adaptive.iot.curval

import kotlinx.coroutines.channels.Channel

class CurValChannelSubscription(
    uuid : CurValSubscriptionId,
    valueIds: List<AioValueId>,
    val channel: Channel<CurVal>
) : CurValSubscription(uuid, valueIds) {

    override fun update(curVal: CurVal) {
        channel.trySend(curVal)
    }

}
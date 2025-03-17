package `fun`.adaptive.iot.curval

import `fun`.adaptive.iot.model.AioValueId
import kotlinx.coroutines.channels.Channel

class CurValSubscription(
    val valueId: AioValueId,
    val channel: Channel<CurVal>
)
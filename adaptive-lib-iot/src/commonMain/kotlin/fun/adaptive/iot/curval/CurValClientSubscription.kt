package `fun`.adaptive.iot.curval

import `fun`.adaptive.log.getLogger
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.trimSignature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

/**
 * Subscriptions from a remote client through a service transport. Calls
 * [CurValApi] on the client side to update the client side value.
 *
 * Stops when the service call fails which will happen as soon as the client
 * closes the connection.
 */
class CurValClientSubscription(
    uuid: CurValSubscriptionId,
    valueIds: List<AioValueId>,
    transport: ServiceCallTransport,
    scope: CoroutineScope,
    capacity: Int = valueIds.size + 1000
) : CurValSubscription(uuid, valueIds) {

    companion object {
        val logger = getLogger(CurValClientSubscription.typeSignature().trimSignature())
    }

    val channel = Channel<CurVal>(capacity)
    val service = getService<CurValApi>(transport)

    init {
        scope.launch {
            run()
        }
    }

    suspend fun run() {
        try {
            for (update in channel) {
                service.update(update)
            }
        } catch (e: Exception) {
            logger.warning(e)
            stop()
        }
    }

    override fun update(curVal: CurVal) {
        val success = channel.trySend(curVal).isSuccess

        if (! success) {
            logger.warning("Channel full, dropping update")
            stop()
        }
    }

    fun stop() {
        worker?.unsubscribe(this.uuid)
        channel.close()
    }

}
package `fun`.adaptive.value

import `fun`.adaptive.value.operation.AvValueOperation
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
 * [AvValueApi] on the client side to update the client side value.
 *
 * Stops when the service call fails which will happen as soon as the client
 * closes the connection.
 */
class AvClientSubscription(
    uuid: AvValueSubscriptionId,
    conditions: List<AvSubscribeCondition>,
    transport: ServiceCallTransport,
    scope: CoroutineScope,
    capacity: Int = conditions.size + 1000
) : AvSubscription(uuid, conditions) {

    companion object {
        val logger = getLogger(AvClientSubscription.typeSignature().trimSignature())
    }

    val channel = Channel<AvValueOperation>(capacity)
    val service = getService<AvValueApi>(transport)

    init {
        scope.launch {
            run()
        }
    }

    suspend fun run() {
        try {
            for (operation in channel) {
                service.process(operation)
            }
        } catch (e: Exception) {
            logger.warning(e)
            stop()
        }
    }

    override fun send(value: AvValueOperation) {
        val success = channel.trySend(value).isSuccess

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
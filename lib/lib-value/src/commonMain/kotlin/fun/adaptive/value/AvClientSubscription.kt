package `fun`.adaptive.value

import `fun`.adaptive.value.operation.AvValueOperation
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.trimSignature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.TimeoutCancellationException
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
    uuid: AvSubscriptionId,
    val roles : Set<UUID<*>>,
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

    override fun add(value : AvValue<*>) {
        if (value.acl != null && ! roles.contains(value.acl)) return
        super.add(value)
    }

    suspend fun run() {
        try {
            for (operation in channel) {
                service.process(operation)
            }
        } catch (_ : TimeoutCancellationException) {
            // not much to do with this one, couldn't send the message, most probably the client disconnected
            stop()
        } catch (e: Exception) {
            logger.warning(e)
            stop()
        }
    }

    override fun send(operation: AvValueOperation) {
        val success = channel.trySend(operation).isSuccess

        if (! success) {
            logger.warning("Channel full, dropping update")
            stop()
        }
    }

    fun stop() {
        store?.unsubscribe(this.uuid)
        channel.close()
    }

}
package `fun`.adaptive.iot.value

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel

class AioValueWorker internal constructor() : WorkerImpl<AioValueWorker> {

    private val lock = getLock()

    private val updates = Channel<AioValue>(Channel.UNLIMITED)

    private val values = mutableMapOf<AioValueId, AioValue>()

    private val index = mutableMapOf<AioValueId, MutableList<AioValueSubscription>>()

    private val subscriptions = mutableMapOf<AuiValueSubscriptionId, AioValueSubscription>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val isIdle: Boolean
        get() = updates.isEmpty

    override suspend fun run() {
        for (value in updates) {
            lock.use {
                val current = values[value.uuid]
                if (current == null || current.timestamp < value.timestamp) {
                    values[value.uuid] = value
                    index[value.uuid]?.forEach { updateSubscription(it, value) }
                }
            }
        }
    }

    private fun updateSubscription(subscription: AioValueSubscription, value: AioValue) {
        try {
            subscription.update(value)
        } catch (e: Exception) {
            logger.warning(e)
            unsubscribe(subscription.uuid)
        }
    }

    fun subscribe(subscription: AioValueSubscription) {
        lock.use {
            unsafeSubscribe(subscription)
        }
    }

    fun subscribe(subscriptions: List<AioValueSubscription>) {
        lock.use {
            for (subscription in subscriptions) {
                unsafeSubscribe(subscription)
            }
        }
    }

    private fun unsafeSubscribe(subscription: AioValueSubscription) {
        subscription.worker = this

        this.subscriptions[subscription.uuid] = subscription

        for (valueId in subscription.valueIds) {
            this.index
                .getOrPut(valueId) { mutableListOf() }
                .add(subscription)

            values[valueId]?.let {
                subscription.update(it)
            }
        }
    }

    fun unsubscribe(subscriptionId: AuiValueSubscriptionId) {
        lock.use {
            unsafeUnsubscribe(subscriptionId)
        }
    }

    private fun unsafeUnsubscribe(subscriptionId: AuiValueSubscriptionId) {
        subscriptions.remove(subscriptionId)?.let {
            for (valueId in it.valueIds) {
                index[valueId]?.remove(it)
            }
            it.worker = null
        }
    }

    fun subscriptionCount(valueId: AioValueId): Int =
        lock.use {
            index[valueId]?.size ?: 0
        }

    operator fun get(valueId: AioValueId): AioValue? =
        lock.use {
            values[valueId]
        }

    fun query(filterFun: (AioValue) -> Boolean): List<AioValue> =
        lock.use {
            values.values.filter(filterFun)
        }

    fun update(value: AioValue) {
        check(updates.trySend(value).isSuccess)
    }

    fun close() {
        updates.close()
    }

}
package `fun`.adaptive.iot.curval

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel

class CurValWorker internal constructor() : WorkerImpl<CurValWorker> {

    private val lock = getLock()

    private val updates = Channel<CurVal>(Channel.UNLIMITED)

    private val values = mutableMapOf<AioValueId, CurVal>()

    private val index = mutableMapOf<AioValueId, MutableList<CurValSubscription>>()

    private val subscriptions = mutableMapOf<CurValSubscriptionId, CurValSubscription>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val isIdle: Boolean
        get() = updates.isEmpty

    override suspend fun run() {
        for (curVal in updates) {
            lock.use {
                val current = values[curVal.uuid]
                if (current == null || current.timestamp < curVal.timestamp) {
                    values[curVal.uuid] = curVal
                    index[curVal.uuid]?.forEach { updateSubscription(it, curVal) }
                }
            }
        }
    }

    private fun updateSubscription(subscription: CurValSubscription, curVal: CurVal) {
        try {
            subscription.update(curVal)
        } catch (e: Exception) {
            logger.warning(e)
            unsubscribe(subscription.uuid)
        }
    }

    fun subscribe(subscription: CurValSubscription) {
        lock.use {
            unsafeSubscribe(subscription)
        }
    }

    fun subscribe(subscriptions: List<CurValSubscription>) {
        lock.use {
            for (subscription in subscriptions) {
                unsafeSubscribe(subscription)
            }
        }
    }

    private fun unsafeSubscribe(subscription: CurValSubscription) {
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

    fun unsubscribe(subscriptionId: CurValSubscriptionId) {
        lock.use {
            unsafeUnsubscribe(subscriptionId)
        }
    }

    private fun unsafeUnsubscribe(subscriptionId: CurValSubscriptionId) {
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

    operator fun get(valueId: AioValueId): CurVal? = values[valueId]

    fun update(value: CurVal) {
        check(updates.trySend(value).isSuccess)
    }

    fun close() {
        updates.close()
    }

}
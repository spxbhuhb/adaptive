package `fun`.adaptive.iot.curval

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.iot.model.AioValueId
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import kotlinx.coroutines.channels.Channel

class CurValWorker : WorkerImpl<CurValWorker> {

    private val lock = getLock()

    internal val updates = Channel<CurVal>(Channel.UNLIMITED)

    internal val values = mutableMapOf<AioValueId, CurVal>()

    internal val subscriptions = mutableMapOf<AioValueId, MutableList<CurValSubscription>>()

    override suspend fun run() {
        for (update in updates) {
            lock.use {
                val current = values[update.uuid]
                if (current == null || current.timestamp < update.timestamp) {
                    values[update.uuid] = update
                    subscriptions[update.uuid]?.forEach { it.channel.send(update) }
                }
            }
        }
    }

    fun subscribe(subscriptions: List<CurValSubscription>) {
        lock.use {
            for (subscription in subscriptions) {

                this.subscriptions
                    .getOrPut(subscription.valueId) { mutableListOf() }
                    .add(subscription)

                values[subscription.valueId]?.let {
                    subscription.channel.trySend(it)
                }
            }
        }
    }

    fun unsubscribe(subscriptions: List<CurValSubscription>) {
        lock.use {
            for (subscription in subscriptions) {
                this.subscriptions[subscription.valueId]?.remove(subscription)
            }
        }
    }

    fun update(value: CurVal) {
        check(updates.trySend(value).isSuccess)
    }

}
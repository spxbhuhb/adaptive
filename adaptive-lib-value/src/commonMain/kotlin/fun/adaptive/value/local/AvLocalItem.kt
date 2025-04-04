package `fun`.adaptive.value.local

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.value.*
import `fun`.adaptive.value.item.AvItem
import kotlin.reflect.KClass

open class AvLocalItem<V : Any>(
    val valueId: AvValueId,
    backend: BackendAdapter,
    val publisher: AvPublisher,
    val specClass: KClass<V>
) : AvLocalStore<AvItem<V>?>() {

    override val scope = backend.scope

    override val localWorker = backend.firstImpl<AvValueWorker>()

    override var value: AvItem<V>? = null
        set(_) = unsupported()

    override suspend fun subscribe(id: AvValueSubscriptionId): List<AvSubscribeCondition> =
        publisher.subscribe(id, valueId)

    override suspend fun unsubscribe(id: AvValueSubscriptionId) =
        publisher.unsubscribe(id)

    override fun process(value: AvValue) {
        if (value !is AvItem<*>) return
        if (! specClass.isInstance(value.spec)) return

        @Suppress("UNCHECKED_CAST")
        this.value = value as AvItem<V>

        notifyListeners()
    }
}
package `fun`.adaptive.value.local

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.value.*
import `fun`.adaptive.value.item.AvItem
import kotlin.reflect.KClass

open class AvLocalItemList<V : Any>(
    backend: BackendAdapter,
    val publisher: AvPublisher,
    val specClass: KClass<V>
) : AvLocalStore<List<AvItem<V>>>() {

    override val scope = backend.scope

    override val localWorker = backend.firstImpl<AvValueWorker>()

    private val itemMap = mutableMapOf<AvValueId, AvItem<V>>()

    override suspend fun subscribe(id: AvValueSubscriptionId): List<AvSubscribeCondition> =
        publisher.subscribe(id)

    override suspend fun unsubscribe(id: AvValueSubscriptionId) =
        publisher.unsubscribe(id)

    override fun process(value: AvValue) {
        if (value !is AvItem<*>) return
        if (! specClass.isInstance(value.spec)) return

        @Suppress("UNCHECKED_CAST")
        itemMap[value.uuid] = value as AvItem<V>

        notifyListeners()
    }

    override var value: List<AvItem<V>>
        get() = itemMap.values.toList()
        set(_) = unsupported()

}
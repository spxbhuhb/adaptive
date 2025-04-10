package `fun`.adaptive.value.local

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.value.*
import `fun`.adaptive.value.item.AvItem
import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

/**
 * Subscribe a list of [AvItem<V>], notify the listener when the list or any item
 * in the list changes.
 */
open class AvLocalItemList<V : Any>(
    val specClass: KClass<V>,
    publisher: AvPublisher,
    scope: CoroutineScope,
    localWorker: AvValueWorker
) : AvAbstractStore<List<AvItem<V>>>(publisher, scope, localWorker) {

    constructor(
        specClass: KClass<V>,
        publisher: AvPublisher,
        backend: BackendAdapter
    ) : this(specClass, publisher, backend.scope, backend.firstImpl<AvValueWorker>())

    private val itemMap = mutableMapOf<AvValueId, AvItem<V>>()

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
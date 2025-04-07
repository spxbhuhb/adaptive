package `fun`.adaptive.value.local

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.value.*
import `fun`.adaptive.value.item.AvItem
import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

/**
 * Subscribe a single [AvItem<V>] and notify listeners when the value is updated from
 * the remote.
 */
open class AvLocalItem<V : Any>(
    val specClass: KClass<V>,
    publisher: AvPublisher,
    scope: CoroutineScope,
    localWorker: AvValueWorker
) : AvAbstractStore<AvItem<V>?>(publisher, scope, localWorker) {

    constructor(
        publisher: AvPublisher,
        specClass: KClass<V>,
        backend: BackendAdapter,
    ) : this(specClass, publisher, backend.scope, backend.firstImpl<AvValueWorker>())

    override var value: AvItem<V>? = null
        set(_) = unsupported()

    override fun process(value: AvValue) {
        if (value !is AvItem<*>) return
        if (! specClass.isInstance(value.spec)) return

        @Suppress("UNCHECKED_CAST")
        this.value = value as AvItem<V>

        notifyListeners()
    }
}